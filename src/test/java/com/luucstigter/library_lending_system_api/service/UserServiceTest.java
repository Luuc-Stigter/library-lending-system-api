package com.luucstigter.library_lending_system_api.service;

import com.luucstigter.library_lending_system_api.dto.UserDto;
import com.luucstigter.library_lending_system_api.dto.UserInputDto;
import com.luucstigter.library_lending_system_api.exception.ResourceNotFoundException;
import com.luucstigter.library_lending_system_api.model.User;
import com.luucstigter.library_lending_system_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setUsername("piet");
        user1.setEmail("piet@test.nl");
        user1.setRole("MEMBER");
        user1.setPassword("geheim_wachtwoord_1");

        user2 = new User();
        user2.setUsername("jan");
        user2.setEmail("jan@test.nl");
        user2.setRole("LIBRARIAN");
        user2.setPassword("geheim_wachtwoord_2");
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> result = userService.getAllUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("piet", result.get(0).username);
        assertEquals("jan", result.get(1).username);
    }

    @Test
    void testGetUserSuccess() {
        // Arrange
        when(userRepository.findByUsername("piet")).thenReturn(Optional.of(user1));

        // Act
        UserDto result = userService.getUser("piet");

        // Assert
        assertEquals("piet@test.nl", result.email);
        assertEquals("MEMBER", result.role);
    }

    @Test
    void testGetUserThrowsException() {
        // Arrange
        when(userRepository.findByUsername("onbekend")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUser("onbekend");
        });
    }

    @Test
    void testCreateUser() {
        // Arrange
        UserInputDto inputDto = new UserInputDto();
        inputDto.username = "nieuweling";
        inputDto.password = "wachtwoord123";
        inputDto.email = "nieuw@test.nl";
        inputDto.role = "MEMBER";

        when(passwordEncoder.encode("wachtwoord123")).thenReturn("gehasht_wachtwoord");

        // Vertel de mock-repository dat wanneer 'save' wordt aangeroepen met elk User-object,
        // hij datzelfde object direct moet teruggeven.
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        //Roep de methode aan en vangen het resultaat (de DTO) op
        UserDto resultDto = userService.createUser(inputDto);

        // Assert
        // We kunnen nu direct het resultaat controleren
        assertNotNull(resultDto);
        assertEquals("nieuweling", resultDto.username);
        assertEquals("nieuw@test.nl", resultDto.email);
        assertEquals("MEMBER", resultDto.role);

        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertEquals("gehasht_wachtwoord", savedUser.getPassword());
    }

    @Test
    void testDeleteUserSuccess() {
        // Arrange
        when(userRepository.existsById("piet")).thenReturn(true);
        doNothing().when(userRepository).deleteById("piet");

        // Act
        userService.deleteUser("piet");

        // Assert
        verify(userRepository, times(1)).deleteById("piet");
    }

    @Test
    void testDeleteUserThrowsException() {
        // Arrange
        when(userRepository.existsById("onbekend")).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser("onbekend");
        });
        verify(userRepository, never()).deleteById("onbekend");
    }
}