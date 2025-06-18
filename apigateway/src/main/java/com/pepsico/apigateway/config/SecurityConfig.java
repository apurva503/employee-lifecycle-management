package com.pepsico.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
/**
 * Security configuration for the API Gateway.
 * Configures CSRF, endpoint authorization, and OAuth2 resource server.
 */
@Configuration
public class SecurityConfig {

    /**
     * Defines the security filter chain for the API Gateway.
     * - Disables CSRF
     * - Permits /auth/** and /actuator/** endpoints
     * - Requires authentication for all other endpoints
     * - Configures OAuth2 JWT resource server
     *
     * @param http the ServerHttpSecurity to configure
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/auth/**", "/actuator/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        String secret = "bXlTZWNyZXRLZXkxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMg==";
        return NimbusReactiveJwtDecoder.withSecretKey(
            new SecretKeySpec(Base64.getDecoder().decode(secret), "HmacSHA256")
        ).build();
    }
}
