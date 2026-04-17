package com.carrental.repository;

import com.carrental.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VehicleRepository
        extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByCarVariantId(Long variantId);
    List<Vehicle> findByCarVariantIdAndStatus(
            Long variantId, String status);
}