package com.fawry.travel_managment.controller;

import com.fawry.travel_managment.dto.DestinationRequestDto;
import com.fawry.travel_managment.entity.Destination;
import com.fawry.travel_managment.service.DestinationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/destinations")
@RequiredArgsConstructor
public class AdminDestinationController {

    private final DestinationService destinationService;

    // 1) GET /api/admin/destinations?query=egypt 
    @GetMapping
    public ResponseEntity<Object> searchExternalAPI(@RequestParam String query) {
        return ResponseEntity.ok(destinationService.fetchCountriesFromExternalApi(query));
    }

    // 2) POST /api/admin/destinations 
    @PostMapping
    public ResponseEntity<Destination> addDestination(@Valid @RequestBody DestinationRequestDto dto) {
        Destination newDestination = new Destination();
        newDestination.setCountry(dto.getCountry());
        newDestination.setCapital(dto.getCapital());
        newDestination.setRegion(dto.getRegion());
        newDestination.setPopulation(dto.getPopulation());
        newDestination.setCurrency(dto.getCurrency());
        newDestination.setFlagImageUrl(dto.getFlagImageUrl());
        Destination savedDestination = destinationService.addDestination(newDestination);
        return ResponseEntity.ok(savedDestination);
    }

    // 3) POST /api/admin/destinations/bulk 
    @PostMapping("/bulk")
    public ResponseEntity<List<Destination>> addBulkDestinations(@RequestBody List<Destination> destinations) {
        return ResponseEntity.ok(destinationService.saveAllDestinations(destinations));
    }

    // 4) DELETE /api/admin/destinations/:id 
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDestination(@PathVariable UUID id) {
        destinationService.deleteDestination(id);
        return ResponseEntity.ok(Map.of("message", "Destination deleted successfully"));
    }
}