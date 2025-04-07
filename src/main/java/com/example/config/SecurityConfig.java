package com.example.config;

import com.example.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/users/register", "/users/login").permitAll()

                // Car management (admin only)
                .requestMatchers(HttpMethod.POST, "/cars").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/cars/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/cars/**").hasAuthority("ROLE_ADMIN")

                // Car viewing (authenticated users)
                .requestMatchers(HttpMethod.GET, "/cars/**").authenticated()

                // Booking endpoints
                .requestMatchers(HttpMethod.GET, "/api/bookings").hasAuthority("ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/bookings/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/bookings/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/bookings").hasAuthority("ROLE_USER")
                .requestMatchers(HttpMethod.DELETE, "/api/bookings/**").hasAuthority("ROLE_ADMIN")

                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .userDetailsService(userDetailsService)
            .httpBasic(httpBasic -> {}); // Use HTTP Basic Auth for Postman

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
