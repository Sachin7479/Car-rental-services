package com.carrental.repository;

import com.carrental.model.CarVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarVariantRepository
        extends JpaRepository<CarVariant, Long> {
    List<CarVariant> findByCompanyId(Long companyId);
}