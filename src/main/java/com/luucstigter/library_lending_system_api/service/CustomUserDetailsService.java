package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.model.User;
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Zoek de gebruiker in de database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden met gebruikersnaam:: " + username));

        // 2. Maak een lijst van autoriteiten (rollen)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // 3. Geef een Spring Security User-object terug
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}