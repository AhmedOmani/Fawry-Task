package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.dto.UserLoginDto;
import com.fawry.travel_managment.dto.UserRegistrationDto;
import com.fawry.travel_managment.entity.Role;
import com.fawry.travel_managment.entity.User;
import com.fawry.travel_managment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto dto) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already taken!");
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setRole(Role.USER); 

        userService.registerUser(newUser);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginDto dto) {
        Optional<User> userOpt = userService.findByEmail(dto.getEmail());
        
        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            // returning token for testing for now
            return ResponseEntity.ok("Login successful! Token: mock-jwt-token-123");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }
}