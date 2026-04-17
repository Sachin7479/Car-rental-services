package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "car_variant")
public class CarVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "model_number")
    private String modelNumber;

    private Integer year;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "is_ac")
    private Boolean isAc;

    @Column(name = "seat_capacity")
    private Integer seatCapacity;

    @Column(name = "rent_per_day")
    private Double rentPerDay;

    private String image;
}