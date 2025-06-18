package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.dto.UserDto;
import com.luucstigter.library_lending_system_api.dto.UserInputDto;
import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.User;
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return convertUserListToDtoList(users);
    }

    public UserDto getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return convertUserToDto(user);
    }

    public UserDto createUser(UserInputDto userInputDto) {
        User user = new User();
        user.setUsername(userInputDto.username);
        user.setPassword(passwordEncoder.encode(userInputDto.password)); // Belangrijk: hashen!
        user.setEmail(userInputDto.email);
        user.setRole(userInputDto.role);
        user.setMembershipStartDate(LocalDate.now());

        User savedUser = userRepository.save(user);
        return convertUserToDto(savedUser);
    }

    public void deleteUser(String username) {
        if (!userRepository.existsById(username)) {
            throw new ResourceNotFoundException("User not found: " + username);
        }
        userRepository.deleteById(username);
    }

    // Private helper methods om te mappen
    private UserDto convertUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.username = user.getUsername();
        dto.email = user.getEmail();
        dto.role = user.getRole();
        return dto;
    }

    private List<UserDto> convertUserListToDtoList(List<User> users) {
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(convertUserToDto(user));
        }
        return dtos;
    }
}