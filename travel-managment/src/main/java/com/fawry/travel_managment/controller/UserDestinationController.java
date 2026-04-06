package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.entity.Destination;
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

    // 1) GET /api/destinations/:id (Returns single approved dest)
    @GetMapping("/{id}")
    public ResponseEntity<Destination> getSingleDestination(@PathVariable UUID id) {
        // You'll need to add findById in DestinationService if you haven't!
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }

    // 2) GET /api/destinations?search=&page=0&size=10 (Returns all/searched)
    @GetMapping
    public ResponseEntity<Page<Destination>> getDestinations(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(destinationService.getDestinations(search, PageRequest.of(page, size)));
    }

    // 3) POST /api/destinations/:id/want-to-visit (Mark dest as wanted)
    @PostMapping("/{id}/want-to-visit")
    public ResponseEntity<String> markWantToVisit(
            @PathVariable UUID id, 
            @RequestHeader("X-User-Id") UUID userId) { // Temp header for Postman testing
        
        userService.addWantedDestination(userId, id);
        return ResponseEntity.ok("Destination added to wishlist!");
    }

    // 4) GET /api/destinations/want-to-visit (Get wishlist)
    @GetMapping("/want-to-visit")
    public ResponseEntity<Set<Destination>> getWantedDestinations(
            @RequestHeader("X-User-Id") UUID userId) { // Temp header for Postman testing
        
        return ResponseEntity.ok(userService.getWantedDestinations(userId));
    }

    // 5) DELETE /api/destinations/:id/want-to-visit (Unmark a dest)
    @DeleteMapping("/{id}/want-to-visit")
    public ResponseEntity<String> removeWantedDestination(
            @PathVariable UUID id, 
            @RequestHeader("X-User-Id") UUID userId) { // Temp header for Postman testing
        
        userService.removeWantedDestination(userId, id);
        return ResponseEntity.ok("Destination removed from wishlist.");
    }
}