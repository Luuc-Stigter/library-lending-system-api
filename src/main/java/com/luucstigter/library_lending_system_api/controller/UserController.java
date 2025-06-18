package com.luucstigter.library_lending_system_api.controller;

import com.luucstigter.library_lending_system_api.dto.UserDto;
import com.luucstigter.library_lending_system_api.dto.UserInputDto;
import com.luucstigter.library_lending_system_api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserInputDto userInputDto) {
        UserDto newUser = userService.createUser(userInputDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{username}").buildAndExpand(newUser.username).toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}