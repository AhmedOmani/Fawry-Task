package com.fawry.travel_managment.config;

import com.fawry.travel_managment.entity.Role;
import com.fawry.travel_managment.entity.User;
import com.fawry.travel_managment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@fawry.com").isEmpty()) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@fawry.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin seeded: admin@fawry.com / admin123");
        }
    }
}
