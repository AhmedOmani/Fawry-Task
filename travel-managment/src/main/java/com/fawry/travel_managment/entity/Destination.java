package com.fawry.travel_managment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Entity
@Table(name= "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String country;

    private String capital;
    
    private String region;
    
    private Long population;
    
    private String currency;
    
    private String flagImageUrl;
}
