package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.dto.UserLoginDto;
import com.fawry.travel_managment.dto.UserRegistrationDto;
import com.fawry.travel_managment.entity.Role;
import com.fawry.travel_managment.entity.User;
import com.fawry.travel_managment.security.JwtUtil;
import com.fawry.travel_managment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto dto) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already taken!"));
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setRole(Role.USER);

        User savedUser = userService.registerUser(newUser);
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully!",
                "token", token
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto dto) {
        Optional<User> userOpt = userService.findByEmail(dto.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId());

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful!",
                    "token", token,
                    "role", user.getRole().name()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
    }
}