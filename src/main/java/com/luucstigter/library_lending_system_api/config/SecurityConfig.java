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
                        // Alleen een LIBRARIAN mag users bekijken, aanmaken of verwijderen
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("LIBRARIAN")

                        // Boeken en items mogen door iedereen met een geldig account bekeken worden
                        .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/**", "/api/items/**").authenticated()

                        // Alleen een LIBRARIAN mag boeken of items aanmaken/wijzigen/verwijderen
                        .requestMatchers("/api/books/**", "/api/items/**").hasRole("LIBRARIAN")

                        // Alleen een LIBRARIAN mag bestanden uploaden
                        .requestMatchers(HttpMethod.POST, "/api/items/**/file").hasRole("LIBRARIAN")

                        // Iedereen die ingelogd is mag downloaden
                        .requestMatchers(HttpMethod.GET, "/api/download/**").authenticated()

                        // Elke andere request moet ingelogd zijn
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());
        return http.build();
    }
}