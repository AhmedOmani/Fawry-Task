package com.fawry.travel_managment.service;

import com.fawry.travel_managment.entity.Destination;
import com.fawry.travel_managment.repository.DestinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}
