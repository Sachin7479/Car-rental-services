package com.carrental.repository;

import com.carrental.model.DrivingLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DrivingLicenseRepository
        extends JpaRepository<DrivingLicense, Long> {
    Optional<DrivingLicense> findByCustomerId(
            Long customerId);
}