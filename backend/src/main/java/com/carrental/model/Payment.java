package com.carrental.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Column(name = "payment_status")
    private String paymentStatus = "Pending";

    @Column(name = "transaction_ref")
    private String transactionRef;

    private Double amount;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "razorpay_order_id")
    private String razorpayOrderId;

    @Column(name = "razorpay_payment_id")
    private String razorpayPaymentId;
}