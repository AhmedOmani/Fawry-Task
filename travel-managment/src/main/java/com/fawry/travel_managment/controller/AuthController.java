package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.dto.UserLoginDto;
import com.fawry.travel_managment.dto.UserRegistrationDto;
import com.fawry.travel_managment.entity.RefreshToken;
import com.fawry.travel_managment.entity.Role;
import com.fawry.travel_managment.entity.User;
import com.fawry.travel_managment.security.JwtUtil;
import com.fawry.travel_managment.service.RefreshTokenService;
import com.fawry.travel_managment.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto dto,
                                          HttpServletResponse response) {
        if (userService.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is already taken!"));
        }

        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(dto.getPassword());
        newUser.setRole(Role.USER);

        User savedUser = userService.registerUser(newUser);
        String accessToken = jwtUtil.generateAccessToken(savedUser.getEmail(), savedUser.getRole().name(), savedUser.getId());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(savedUser.getId());
        addRefreshTokenCookie(response, refreshToken.getToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "User registered successfully!",
                "token", accessToken
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginDto dto,
                                       HttpServletResponse response) {
        Optional<User> userOpt = userService.findByEmail(dto.getEmail());

        if (userOpt.isPresent() && passwordEncoder.matches(dto.getPassword(), userOpt.get().getPassword())) {
            User user = userOpt.get();
            String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name(), user.getId());

            // clear old refresh tokens for this user before issuing a new one
            refreshTokenService.deleteByUserId(user.getId());
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
            addRefreshTokenCookie(response, refreshToken.getToken());

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful!",
                    "token", accessToken,
                    "role", user.getRole().name()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenStr = extractRefreshTokenFromCookie(request);

        if (refreshTokenStr == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No refresh token provided"));
        }

        return refreshTokenService.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    User user = refreshToken.getUser();
                    String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name(), user.getId());
                    return ResponseEntity.ok(Map.of(
                            "token", newAccessToken,
                            "role", user.getRole().name()
                    ));
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid refresh token")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenStr = extractRefreshTokenFromCookie(request);
        if (refreshTokenStr != null) {
            refreshTokenService.findByToken(refreshTokenStr)
                    .ifPresent(rt -> refreshTokenService.deleteByUserId(rt.getUser().getId()));
        }
        // clear the cookie
        Cookie cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
        response.addCookie(cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}