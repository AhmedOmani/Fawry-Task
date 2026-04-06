package com.fawry.travel_managment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DestinationRequestDto {

    @NotBlank(message = "Country name is required")
    private String country;

    @NotBlank(message = "Capital is required")
    private String capital;

    private String region;
    
    private Long population;
    
    private String currency;
    
    private String flagImageUrl;
}