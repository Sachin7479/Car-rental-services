package com.carrental.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String contact;
    private String address;
    private String licenseNumber;
    private String licenseExpiry;
}