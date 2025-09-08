package com.hackathon.recruiting_app_backend.security.config;

import com.hackathon.recruiting_app_backend.security.filter.JwtAuthenticationFilter;
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
//                .csrf(csrf -> csrf.disable())
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
                        .requestMatchers(HttpMethod.GET, "/api/job-applications/geCandidateJobApplications").hasRole("CANDIDATE")
                        .requestMatchers(HttpMethod.GET, "/api/job-applications/getJobsApplicationsForRecruiters").hasRole("RECRUITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/job-applications/withdrawApplication/*").hasAnyRole("CANDIDATE", "RECRUITER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/job-applications/deleteJobApplication/*").hasAnyRole("ADMIN", "RECRUITER")
                        .requestMatchers(HttpMethod.PUT, "/api/job-applications/updateApplicationStatus/*").hasAnyRole("RECRUITER", "ADMIN")

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
