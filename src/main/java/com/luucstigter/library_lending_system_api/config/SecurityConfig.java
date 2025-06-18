package com.luucstigter.library_lending_system_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpMethod;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // User management
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("LIBRARIAN")

                        // File I/O
                        .requestMatchers(HttpMethod.POST, "/api/items/*/file").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/download/**").authenticated()

                        // Loan management
                        .requestMatchers(HttpMethod.POST, "/api/loans/item/**").hasRole("MEMBER")

                        // Book & Item viewing
                        .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**", "/api/items/**").authenticated()

                        // Book & Item management
                        .requestMatchers("/api/books/**", "/api/items/**").hasRole("LIBRARIAN")

                        // Fallback
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}