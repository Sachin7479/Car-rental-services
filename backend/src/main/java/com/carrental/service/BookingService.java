package com.carrental.service;

import com.carrental.dto.BookingRequest;
import com.carrental.model.Booking;
import com.carrental.model.CarVariant;
import com.carrental.model.Customer;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.CarVariantRepository;
import com.carrental.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarVariantRepository carVariantRepository;

    public Map<String, Object> createBooking(
            BookingRequest request) {
        Map<String, Object> response = new HashMap<>();

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElse(null);

        if (customer == null) {
            response.put("success", false);
            response.put("message", "Customer not found!");
            return response;
        }

        Booking booking = new Booking();
        booking.setBookingId(generateBookingId());
        booking.setCustomer(customer);
        booking.setCarName(request.getCarName());
        booking.setCarImage(request.getCarImage());
        booking.setFromDate(LocalDate.parse(
                request.getFromDate()));
        booking.setToDate(LocalDate.parse(
                request.getToDate()));
        booking.setTotalDays(request.getTotalDays());
        booking.setTotalPrice(request.getTotalPrice());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("Pending");
        booking.setPaymentStatus("Pending");

        if (request.getVariantId() != null) {
            CarVariant variant = carVariantRepository
                    .findById(request.getVariantId())
                    .orElse(null);
            booking.setCarVariant(variant);
            if (variant != null) {
                if (booking.getCarName() == null ||
                        booking.getCarName().isBlank()) {
                    booking.setCarName(variant.getName());
                }
                if (booking.getCarImage() == null ||
                        booking.getCarImage().isBlank()) {
                    booking.setCarImage(variant.getImage());
                }
            }
        }

        bookingRepository.save(booking);

        response.put("success", true);
        response.put("message",
                "Booking initiated successfully! Your request is pending admin approval.");
        response.put("booking", mapBooking(booking));
        return response;
    }

    public List<Map<String, Object>> getCustomerBookings(
            Long customerId) {
        return bookingRepository.findByCustomerId(customerId)
                .stream()
                .sorted(Comparator.comparing(
                        Booking::getBookingTime,
                        Comparator.nullsLast(
                                Comparator.reverseOrder())))
                .map(this::mapBooking)
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(
                        Booking::getBookingTime,
                        Comparator.nullsLast(
                                Comparator.reverseOrder())))
                .map(this::mapBooking)
                .collect(Collectors.toList());
    }

    public Map<String, Object> updateBookingStatus(
            String bookingId,
            String status) {
        Map<String, Object> response = new HashMap<>();

        Booking booking = bookingRepository
                .findByBookingId(bookingId)
                .orElse(null);

        if (booking == null) {
            response.put("success", false);
            response.put("message", "Booking not found!");
            return response;
        }

        booking.setStatus(status);
        if ("Approved".equalsIgnoreCase(status)) {
            booking.setPaymentStatus("Pending");
        }
        if ("Cancelled".equalsIgnoreCase(status)) {
            booking.setPaymentStatus("Cancelled");
        }

        bookingRepository.save(booking);

        response.put("success", true);
        response.put("message",
                "Booking status updated successfully.");
        response.put("booking", mapBooking(booking));
        return response;
    }

    private Map<String, Object> mapBooking(Booking booking) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", booking.getBookingId());
        item.put("dbId", booking.getId());
        item.put("car", booking.getCarName());
        item.put("img", booking.getCarImage());
        item.put("customerId",
                booking.getCustomer() != null
                        ? booking.getCustomer().getId()
                        : null);
        item.put("customer",
                booking.getCustomer() != null
                        ? booking.getCustomer().getFirstname() +
                        " " +
                        booking.getCustomer().getLastname()
                        : "");
        item.put("from",
                booking.getFromDate() != null
                        ? booking.getFromDate().toString()
                        : "");
        item.put("to",
                booking.getToDate() != null
                        ? booking.getToDate().toString()
                        : "");
        item.put("days", booking.getTotalDays());
        item.put("price", booking.getTotalPrice());
        item.put("status", booking.getStatus());
        item.put("vehicle",
                booking.getVehicle() != null
                        ? booking.getVehicle()
                        .getRegistrationNumber()
                        : "NA");
        item.put("payment",
                booking.getPaymentStatus() != null
                        ? booking.getPaymentStatus()
                        : "Pending");
        item.put("bookingTime",
                booking.getBookingTime() != null
                        ? booking.getBookingTime().toString()
                        : "");
        return item;
    }

    private String generateBookingId() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }
}
