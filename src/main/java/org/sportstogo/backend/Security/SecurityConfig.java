package org.sportstogo.backend.Security;

import org.sportstogo.backend.Service.FirebaseTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final FirebaseTokenService firebaseTokenService;

    public SecurityConfig(FirebaseTokenService firebaseTokenService) {
        this.firebaseTokenService = firebaseTokenService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        FirebaseAuthenticationFilter firebaseFilter = new FirebaseAuthenticationFilter(firebaseTokenService);

        return http
                .csrf(CsrfConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/profile/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/profile").authenticated()
                        // this order FUCKING matters SO FUCKING MUCH DON'T TOUCH THIS OR I'M GOING TO HUNT YOU DOWN
                        .requestMatchers("/social/chat/**").permitAll()
                        .requestMatchers("/social/**").authenticated()
                        .anyRequest().permitAll()
                )
                .addFilterBefore(firebaseFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Allow the frontend origin
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Added OPTIONS
        configuration.setAllowedHeaders(List.of("*")); // Allow all headers for WebSocket
        configuration.setAllowCredentials(true); // Allow cookies or credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply the configuration to all endpoints
        return source;
    }
}