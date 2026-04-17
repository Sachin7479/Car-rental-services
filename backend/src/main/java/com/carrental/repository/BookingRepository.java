package com.carrental.repository;

import com.carrental.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository
        extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerId(Long customerId);
    Optional<Booking> findByBookingId(String bookingId);
    List<Booking> findByStatus(String status);
}