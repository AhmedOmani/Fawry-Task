package com.fawry.travel_managment.service;

import com.fawry.travel_managment.entity.Destination;
import com.fawry.travel_managment.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DestinationService {
    private final DestinationRepository destinationRepository;
    private final RestTemplate restTemplate;
    
    public List<Destination> getAllApprovedDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(UUID id) {
        return destinationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Destination not found"));
    }

    public Page<Destination> getDestinations(String search, Pageable pageable) {
        if (search != null && !search.isBlank()) {
            return destinationRepository.findByCountryContainingIgnoreCase(search, pageable);
        }
        return destinationRepository.findAll(pageable);
    }

    public Destination addDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public void deleteDestination(UUID id) {
        destinationRepository.deleteById(id);
    }

    public Object fetchCountriesFromExternalApi (String countryName) {
        String url = "https://restcountries.com/v3.1/name/" + countryName;
        return restTemplate.getForObject(url, Object.class);
    }

    public List<Destination> saveAllDestinations(List<Destination> destinations) {
        return destinationRepository.saveAll(destinations);
    }
}
