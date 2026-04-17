package com.carrental.controller;

import com.carrental.dto.BookingRequest;
import com.carrental.dto.BookingStatusUpdateRequest;
import com.carrental.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = { RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS })
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(
            @RequestBody BookingRequest request) {
        return ResponseEntity.ok(
                bookingService.createBooking(request));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerBookings(
            @PathVariable Long customerId) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("bookings",
                bookingService.getCustomerBookings(customerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBookings() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("bookings",
                bookingService.getAllBookings());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Map<String, Object>> updateBookingStatus(
            @PathVariable String bookingId,
            @RequestBody BookingStatusUpdateRequest request) {
        return ResponseEntity.ok(
                bookingService.updateBookingStatus(
                        bookingId,
                        request.getStatus()));
    }
}
