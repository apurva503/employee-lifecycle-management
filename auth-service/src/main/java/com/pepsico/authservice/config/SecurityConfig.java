package com.pepsico.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Auth Service.
 * Configures CSRF, endpoint authorization, and security filter chain.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Defines the security filter chain for the Auth Service.
     * - Disables CSRF
     * - Permits /auth/login endpoint
     * - Requires authentication for all other endpoints
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if a security error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll() 
                .anyRequest().authenticated()
            );

        return http.build();
    }

}
