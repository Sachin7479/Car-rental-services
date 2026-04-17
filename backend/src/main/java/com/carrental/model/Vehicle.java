package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private CarVariant carVariant;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    private String status = "ACTIVE";
}