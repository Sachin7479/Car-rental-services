package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;
    private String contact;
    private String address;

    @Column(name = "role")
    private String role = "CUSTOMER";
}