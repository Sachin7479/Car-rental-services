package com.carrental.dto;

import lombok.Data;

@Data
public class BookingRequest {
    private Long customerId;
    private Long variantId;
    private String carName;
    private String carImage;
    private String fromDate;
    private String toDate;
    private Integer totalDays;
    private Double totalPrice;
}
