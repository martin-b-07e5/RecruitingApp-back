# 🎯 **Steps in order:**

1. **`SecurityConfig.java`** - Basic security configuration
2. **`JwtUtil.java`** - Utilities for JWT (with auth0)
5. **`AuthService.java`** - Authentication logic
3. **`CustomUserDetailsService.java`** - Loads users from DB
4. **`JwtAuthenticationFilter.java`** - Filter to validate tokens
6. **`AuthController.java`** - Login/register endpoints
7. **DTOs** - AuthRequestDTO and AuthResponseDTO

---

## ⚡ **`SecurityConfig.java`** - the heart of security.

**Key points:**

- ✅ Stateless sessions (JWT)
- ✅ CSRF disabled (REST API)
- ✅ Role-based authorization
- ✅ JWT filter integration
- ✅ Password encoder

---

# 🔐 **JwtUtil.java with auth0**

**Key features:**

- ✅ Uses auth0/java-jwt 4.5.0
- ✅ Clean and modern API
- ✅ Token generation with username and role
- ✅ Token validation and claim extraction
- ✅ Configurable secret and expiration

---

# 🔐 **AuthService.java**

**Key features:**

- ✅ Handles login authentication
- ✅ Handles user registration with password encoding
- ✅ Returns JWT tokens after successful auth
- ✅ Uses Spring Security's AuthenticationManager
- ✅ Validates email uniqueness

---

# 🔐 **CustomUserDetailsService.java**

**Key points:**

- ✅ Loads users from database by email
- ✅ Implements Spring Security's UserDetailsService
- ✅ Returns UserDetails with proper role authority
- ✅ Uses UserRepository (you'll need to add findByEmail method)

---

# 🔐 **JwtAuthenticationFilter.java**

**Key features:**

- ✅ Extends `OncePerRequestFilter`
- ✅ Checks for Bearer token in Authorization header
- ✅ Validates JWT using `JwtUtil`
- ✅ Sets authentication in SecurityContext
- ✅ Uses constructor injection for dependencies

---

# 🔐 **AuthController.java**

**Key features:**

- ✅ `/api/auth/login` endpoint
- ✅ `/api/auth/register` endpoint
- ✅ Clean separation with AuthService
- ✅ Returns JWT tokens in responses

---

# 🔐 **AuthRequestDTO.java**

# 🔐 **AuthResponseDTO.java**

**Simple DTOs for:**

- ✅ Login/Register requests (email + password)
- ✅ Auth responses (token + user info)
- ✅ Clean separation between layers
