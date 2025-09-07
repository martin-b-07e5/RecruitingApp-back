### jobApplication.properties

    jwt.secret=yourSuperSecretKeyWithAtLeast256BitsLongForHS256
    # 24 hours
    jwt.expiration=86400000

---

# 🎯 **Next steps in order:**

1. **`SecurityConfig.java`** - Basic security configuration
2. **`JwtUtil.java`** - Utilities for JWT (with auth0)
3. **`CustomUserDetailsService.java`** - Loads users from DB
4. **`JwtAuthenticationFilter.java`** - Filter to validate tokens
5. **`AuthService.java`** - Authentication logic
6. **`AuthController.java`** - Login/register endpoints
7. **DTOs** - AuthRequestDTO and AuthResponseDTO

## ⚡ **Let's start with `SecurityConfig.java`** - the heart of security.

Next `SecurityConfig.java` with auth0?
---

# 🔐 **SecurityConfig.java**

```java
package com.hackathon.recruiting_app_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/recruiter/**").hasRole("RECRUITER")
                        .requestMatchers("/api/candidate/**").hasRole("CANDIDATE")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
```

**Key points:**

- ✅ Stateless sessions (JWT)
- ✅ CSRF disabled (REST API)
- ✅ Role-based authorization
- ✅ JWT filter integration
- ✅ Password encoder

---

### error

'addFilterBefore(jakarta.servlet.Filter, java.lang.Class<? extends jakarta.servlet.Filter>)' in '
org.springframework.security.config.annotation.web.builders.HttpSecurity' cannot be applied to '(
com.hackathon.recruiting_app_backend.security.JwtAuthenticationFilter, java.lang.Class<
org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter>)'

### solution:

**`JwtAuthenticationFilter` must extend `OncePerRequestFilter`** and have the annotation
`@Component`.

## ✅ **Fix JwtAuthenticationFilter:**

```java
package com.hackathon.recruiting_app_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component  // ← Add this annotation
public class JwtAuthenticationFilter extends OncePerRequestFilter {  // ← Extiende de OncePerRequestFilter

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Empty for now, we'll implement it later.
        filterChain.doFilter(request, response);
    }
}
```

### **The error will disappear** when:

1. ✅ You extend `OncePerRequestFilter`
2. ✅ You add `@Component`
3. ✅ You implement the `doFilterInternal` method (even if it is empty for now)

---

# 🔐 **JwtUtil.java with auth0**

```java
package com.hackathon.recruiting_app_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractUsername(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return jwt.getSubject();
    }

    public String extractRole(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return jwt.getClaim("role").asString();
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
```

**Key features:**

- ✅ Uses auth0/java-jwt 4.5.0
- ✅ Clean and modern API
- ✅ Token generation with username and role
- ✅ Token validation and claim extraction
- ✅ Configurable secret and expiration

**Next:** `CustomUserDetailsService.java`

---

# 🔐 **CustomUserDetailsService.java**

```java
package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository UserRepository;

    public CustomUserDetailsService(UserRepository UserRepository) {
        this.UserRepository = UserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = UserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
```

**Key points:**

- ✅ Loads users from database by email
- ✅ Implements Spring Security's UserDetailsService
- ✅ Returns UserDetails with proper role authority
- ✅ Uses UserRepository (you'll need to add findByEmail method)

**Next:** Add findByEmail method to UserRepository

---

# 🔐 **UserRepository.java update**

```java
package com.hackathon.recruiting_app_backend.repository;

import com.hackathon.recruiting_app_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
```

**Key additions:**

- ✅ `findByEmail(String email)` - For authentication
- ✅ `existsByEmail(String email)` - For registration validation
- ✅ Both return `Optional` for null safety

**Next:** `JwtAuthenticationFilter.java` implementation

---

# 🔐 **JwtAuthenticationFilter.java**

```java
package com.hackathon.recruiting_app_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
```

**Key features:**

- ✅ Extends `OncePerRequestFilter`
- ✅ Checks for Bearer token in Authorization header
- ✅ Validates JWT using `JwtUtil`
- ✅ Sets authentication in SecurityContext
- ✅ Uses constructor injection for dependencies

**Next:** `AuthService.java`

---

# 🔐 **AuthService.java**

```java
package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.User;
import com.hackathon.recruiting_app_backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    public String register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());
    }
}
```

**Key features:**

- ✅ Handles login authentication
- ✅ Handles user registration with password encoding
- ✅ Returns JWT tokens after successful auth
- ✅ Uses Spring Security's AuthenticationManager
- ✅ Validates email uniqueness

**Next:** `AuthRequestDTO.java` and `AuthResponseDTO.java`

---

# 🔐 **AuthRequestDTO.java**

```java
package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
```

# 🔐 **AuthResponseDTO.java**

```java
package com.hackathon.recruiting_app_backend.security;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String email;
    private String role;

    public AuthResponseDTO(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }
}
```

**Simple DTOs for:**

- ✅ Login/Register requests (email + password)
- ✅ Auth responses (token + user info)
- ✅ Clean separation between layers

**Next:** `AuthController.java` endpoints

---

# 🔐 **AuthController.java**

```java
package com.hackathon.recruiting_app_backend.security;

import com.hackathon.recruiting_app_backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        // You'll need to add user details lookup here for the response
        return ResponseEntity.ok(new AuthResponseDTO(token, request.getEmail(), "ROLE"));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody User user) {
        String token = authService.register(user);
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getRole().name()));
    }
}
```

**Key features:**

- ✅ `/api/auth/login` endpoint
- ✅ `/api/auth/register` endpoint
- ✅ Clean separation with AuthService
- ✅ Returns JWT tokens in responses

**Note:** The login response needs user role lookup - you'll need to add that to AuthService.

---

# Note: The login response needs user role lookup - you'll need to add that to AuthService.

# 🔍 **The login response is incomplete**

In your `AuthController.login()` method:

```java
return ResponseEntity.ok(new AuthResponseDTO(token, request.getEmail(), "ROLE"));
```

The `"ROLE"` is hardcoded - you need to **get the actual user's role** from the database.

## ✅ **Fix in AuthService.login():**

```java
public AuthResponseDTO login(String email, String password) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
    );

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found after authentication"));

    String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

    return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
}
```

## ✅ **Then update AuthController:**

```java

@PostMapping("/login")
public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
    AuthResponseDTO response = authService.login(request.getEmail(), request.getPassword());
    return ResponseEntity.ok(response);
}
```

**This way** the service returns the complete response with the actual user role.

---
---

# 🧪 **Testing the JWT Authentication**

## **1. First, add the missing properties to jobApplication.properties:**

```properties
jwt.secret=yourSuperSecretKeyWithAtLeast256BitsLongForHS256
jwt.expiration=86400000
```

## **2. Test with curl or Postman:**

### **Register a user:**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: jobApplication/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "role": "CANDIDATE"
  }'
```

### **Login:**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: jobApplication/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## **3. Expected responses:**

**Register response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "test@example.com",
  "role": "CANDIDATE"
}
```

**Login response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "test@example.com",
  "role": "CANDIDATE"
}
```

## **4. Test protected endpoint:**

```bash
curl -X GET http://localhost:8080/api/candidate/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

**If you get a 200 OK, authentication is working!** 🎉
---
---
---
---

