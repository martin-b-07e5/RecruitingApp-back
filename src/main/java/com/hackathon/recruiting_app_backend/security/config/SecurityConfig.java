package com.hackathon.recruiting_app_backend.security.config;

import com.hackathon.recruiting_app_backend.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                // Auth
                                .requestMatchers("/api/auth/**").permitAll()
                                // Job Offers
                                .requestMatchers(HttpMethod.POST, "/api/job-offers/create").hasRole("RECRUITER")  // Recruiters can post new job opportunities
                                .requestMatchers(HttpMethod.GET, "/api/job-offers/getAllJobOffers").permitAll() // Anyone can view job offers (no login required)
                                .requestMatchers(HttpMethod.GET, "/api/job-offers/getJobOfferById/**").permitAll() // → Anyone can view job offers (no login required)
                                .requestMatchers(HttpMethod.GET, "/api/job-offers/getMyJobOffers").hasRole("RECRUITER")  // Only the recruiter who created the offer can view it
                                .requestMatchers(HttpMethod.PUT, "/api/job-offers/**").hasAnyRole("RECRUITER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/job-offers/*").hasAnyRole("RECRUITER", "ADMIN")
                                // Job Applications
                                .requestMatchers(HttpMethod.POST, "/api/job-applications/apply").hasRole("CANDIDATE")
                                .requestMatchers(HttpMethod.GET, "/api/job-applications/getAllJobApplications").hasAnyRole("ADMIN", "RECRUITER")
                                .requestMatchers(HttpMethod.GET, "/api/job-applications/getJobApplicationById/*").hasAnyRole("ADMIN", "RECRUITER", "CANDIDATE")
                                .requestMatchers(HttpMethod.GET, "/api/job-applications/getCandidateJobApplications").hasRole("CANDIDATE")
                                .requestMatchers(HttpMethod.GET, "/api/job-applications/getJobsApplicationsForRecruiters").hasAnyRole("RECRUITER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/job-applications/updateApplicationStatus/*").hasAnyRole("RECRUITER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/job-applications/withdrawApplication/*").hasAnyRole("CANDIDATE", "RECRUITER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/job-applications/deleteJobApplication/*").hasAnyRole("ADMIN", "RECRUITER")
                                // Users
                                .requestMatchers(HttpMethod.PUT, "/api/users/update/**").hasAnyRole("ADMIN", "RECRUITER", "CANDIDATE")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/delete/**").hasAnyRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/users/delete-self").hasAnyRole("RECRUITER", "CANDIDATE")

                                .requestMatchers(HttpMethod.GET, "/api/users/getAllUsers").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/users/getUserById/**").hasAnyRole("ADMIN", "RECRUITER", "CANDIDATE")
                                .requestMatchers(HttpMethod.GET, "/api/users/companies").hasRole("RECRUITER")
//                        .requestMatchers(HttpMethod.GET, "/api/users/getUserByEmail/**").hasAnyRole("ADMIN", "RECRUITER", "CANDIDATE")
//                        .requestMatchers(HttpMethod.GET, "/api/users/getUserByRole/**").hasAnyRole("ADMIN", "RECRUITER", "CANDIDATE")

                                // All
                                .anyRequest().authenticated()  // for production
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.getWriter().write("Unauthorized: Token is missing or invalid");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.getWriter().write("Forbidden: You do not have permission to access this resource");
                        })
                )
        ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // Frontend dev
        configuration.addAllowedOrigin("http://localhost:3001"); // Frontend dockerized
        configuration.addAllowedOrigin("http://146.235.58.90:3002"); // Frontend dockerized remote
        configuration.addAllowedOrigin("http://hackaton202508.duckdns.org:3002"); // Frontend DNS
        configuration.addAllowedOrigin("https://hackaton202508.duckdns.org:3002"); // Frontend DNS
//        configuration.addAllowedOriginPattern("*"); // Temp for testing
        configuration.addAllowedMethod("*"); // Allow all HTTP methods
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true); // Allow cookies, auth headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
