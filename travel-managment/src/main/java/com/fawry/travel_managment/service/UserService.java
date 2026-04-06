package com.fawry.travel_managment.service;

import com.fawry.travel_managment.entity.Destination;
import com.fawry.travel_managment.entity.User;
import com.fawry.travel_managment.repository.DestinationRepository;
import com.fawry.travel_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addWantedDestinations(UUID userId , UUID destinationId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

        Destination destination = destinationRepository.findById(destinationId)
        .orElseThrow(() -> new RuntimeException("Destination not found"));

        user.getWantedDestinations().add(destination);
        userRepository.save(user);
    }

    public Set<Destination> getWantedDestinations(UUID userId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWantedDestinations();
    }

    public void removeWantedDestinations(UUID userId , UUID destinationId) {
        User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

        Destination destination = destinationRepository.findById(destinationId)
        .orElseThrow(() -> new RuntimeException("Destination not found"));

        user.getWantedDestinations().remove(destination);
        userRepository.save(user);
    }
    
}
