package com.fawry.travel_managment.dto;
import lombok.Data;

@Data
public class UserLoginDto {
    private String email;
    private String password;
}