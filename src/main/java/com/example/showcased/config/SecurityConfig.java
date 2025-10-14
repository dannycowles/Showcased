package com.example.showcased.config;

import com.example.showcased.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request ->
                    request
                            // Require auth on logout and change password endpoint but the others are allowed
                            .requestMatchers(
                                    "/auth/logout",
                                    "/auth/change-password"
                            ).authenticated()
                            .requestMatchers("/auth/**").permitAll()

                            // Auth required for all profile endpoints
                            .requestMatchers("/profile/**").authenticated()

                            // Any POST, DELETE, or PATCH methods on show endpoints require auth
                            .requestMatchers(HttpMethod.DELETE, "/shows/**").authenticated()
                            .requestMatchers(HttpMethod.POST, "/shows/**").authenticated()
                            .requestMatchers(HttpMethod.PATCH, "/shows/**").authenticated()

                            // Any POST or DELETE methods on user endpoints require auth
                            .requestMatchers(HttpMethod.POST, "/users/**").authenticated()
                            .requestMatchers(HttpMethod.DELETE, "/users/**").authenticated()

                            // All other GET endpoints are allowed without auth
                            .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:4200", // Local dev only
                "https://showcased.dev", // Production
                "https://*.vercel.app" // All Vercel preview URLs
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
