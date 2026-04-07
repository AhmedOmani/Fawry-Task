package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.entity.Destination;
import com.fawry.travel_managment.security.JwtUtil;
import com.fawry.travel_managment.service.DestinationService;
import com.fawry.travel_managment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class UserDestinationController {

    private final DestinationService destinationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private UUID getUserIdFromToken(String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        return jwtUtil.extractUserId(token);
    }

    // 1) GET /api/destinations/:id 
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getSingleDestination(@PathVariable UUID id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }

    // 2) GET /api/destinations?search=&page=0&size=10 
    @GetMapping
    public ResponseEntity<Page<Destination>> getDestinations(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(destinationService.getDestinations(search, PageRequest.of(page, size)));
    }

    // 3) POST /api/destinations/:id/want-to-visit 
    @PostMapping("/{id}/want-to-visit")
    public ResponseEntity<String> markWantToVisit(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromToken(authHeader);
        userService.addWantedDestination(userId, id);
        return ResponseEntity.ok("Destination added to wishlist!");
    }

    // 4) GET /api/destinations/want-to-visit 
    @GetMapping("/want-to-visit")
    public ResponseEntity<Set<Destination>> getWantedDestinations(
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromToken(authHeader);
        return ResponseEntity.ok(userService.getWantedDestinations(userId));
    }

    // 5) DELETE /api/destinations/:id/want-to-visit 
    @DeleteMapping("/{id}/want-to-visit")
    public ResponseEntity<String> removeWantedDestination(
            @PathVariable UUID id,
            @RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromToken(authHeader);
        userService.removeWantedDestination(userId, id);
        return ResponseEntity.ok("Destination removed from wishlist.");
    }
}