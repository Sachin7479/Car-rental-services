package com.carrental.controller;

import com.carrental.model.CarVariant;
import com.carrental.repository.CarVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/variants")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = { RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS })
public class VariantController {

    @Autowired
    private CarVariantRepository carVariantRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllVariants() {
        List<Map<String, Object>> response = carVariantRepository
                .findAll()
                .stream()
                .map(this::mapVariant)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVariantById(
            @PathVariable Long id) {
        CarVariant variant = carVariantRepository.findById(id)
                .orElse(null);

        if (variant == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Variant not found!");
            return ResponseEntity.status(404).body(response);
        }

        Map<String, Object> response = mapVariant(variant);
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> mapVariant(CarVariant variant) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", variant.getId());
        item.put("name", variant.getName());
        item.put("description", variant.getDescription());
        item.put("company", variant.getCompany() != null
                ? variant.getCompany().getName()
                : "");
        item.put("companyId", variant.getCompany() != null
                ? variant.getCompany().getId()
                : null);
        item.put("modelNumber", variant.getModelNumber());
        item.put("year", variant.getYear());
        item.put("fuelType", variant.getFuelType());
        item.put("isAc", variant.getIsAc());
        item.put("seatCapacity", variant.getSeatCapacity());
        item.put("rentPerDay", variant.getRentPerDay());
        item.put("image", variant.getImage());
        return item;
    }
}
