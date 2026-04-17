package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id", unique = true)
    private String bookingId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private CarVariant carVariant;

    @Column(name = "car_name")
    private String carName;

    @Column(name = "car_image")
    private String carImage;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    @Column(name = "total_days")
    private Integer totalDays;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "booking_time")
    private LocalDateTime bookingTime;

    private String status = "Pending";

    @Column(name = "payment_status")
    private String paymentStatus = "Pending";
}
