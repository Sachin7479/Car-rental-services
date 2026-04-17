package com.carrental.controller;

import com.carrental.dto.LoginRequest;
import com.carrental.dto.RegisterRequest;
import com.carrental.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = { RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.OPTIONS })
public class AuthController {

    @Autowired
    private AuthService authService;

    // Register API
    // POST http://localhost:8080/api/auth/register
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
            @RequestBody RegisterRequest request) {
        Map<String, Object> response =
                authService.register(request);
        return ResponseEntity.ok(response);
    }

    // Login API
    // POST http://localhost:8080/api/auth/login
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody LoginRequest request) {
        Map<String, Object> response =
                authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile/{customerId}")
    public ResponseEntity<Map<String, Object>> getCustomerProfile(
            @PathVariable Long customerId) {
        Map<String, Object> response =
                authService.getCustomerProfile(
                        customerId);
        return ResponseEntity.ok(response);
    }
}
