package com.fawry.travel_managment.repository;

import com.fawry.travel_managment.entity.Destination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, UUID> {
    Page<Destination> findByCountryContainingIgnoreCase(String country, Pageable pageable);
}
