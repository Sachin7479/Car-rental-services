package com.carrental.service;

import com.carrental.config.JwtUtil;
import com.carrental.dto.LoginRequest;
import com.carrental.dto.RegisterRequest;
import com.carrental.model.*;
import com.carrental.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DrivingLicenseRepository licenseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.admin.default-name:Admin User}")
    private String defaultAdminName;

    @Value("${app.admin.default-email:admin@carrental.com}")
    private String defaultAdminEmail;

    @Value("${app.admin.default-password:Admin@123}")
    private String defaultAdminPassword;

    // Register new customer
    public Map<String, Object> register(
            RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Check if email already exists
        if (customerRepository.existsByEmail(
                request.getEmail())) {
            response.put("success", false);
            response.put("message",
                    "Email already registered!");
            return response;
        }

        // Create new customer
        Customer customer = new Customer();
        customer.setFirstname(request.getFirstname());
        customer.setLastname(request.getLastname());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(
                request.getPassword()));
        customer.setContact(request.getContact());
        customer.setAddress(request.getAddress());
        customer.setRole("CUSTOMER");
        customerRepository.save(customer);

        // Save driving license
        DrivingLicense license = new DrivingLicense();
        license.setCustomer(customer);
        license.setLicenseNumber(
                request.getLicenseNumber());
        license.setExpiryDate(LocalDate.parse(
                request.getLicenseExpiry()));
        licenseRepository.save(license);

        response.put("success", true);
        response.put("message",
                "Registered successfully!");
        return response;
    }

    // Login
    public Map<String, Object> login(
            LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        String requestedRole = normalizeRole(
                request.getRole());
        String email = normalizeEmail(request.getEmail());
        String password = normalizePassword(
                request.getPassword());

        if ("ADMIN".equals(requestedRole)) {
            // Admin login
            Admin admin = findOrCreateDefaultAdmin(
                    email);

            if (isDefaultAdminCredentials(email, password)) {
                admin = ensureDefaultAdmin(email);
            }

            if (admin == null || !matchesPassword(
                    password, admin.getPassword(),
                    email)) {
                response.put("success", false);
                response.put("message",
                        "Invalid email or password!");
                return response;
            }

            String token = jwtUtil.generateToken(
                    admin.getEmail(), "ADMIN");
            response.put("success", true);
            response.put("token", token);
            response.put("role", "ADMIN");
            response.put("name", admin.getName());
            response.put("email", admin.getEmail());
            response.put("id", admin.getId());

        } else {
            // Customer login
            Customer customer = customerRepository
                    .findByEmail(email)
                    .orElse(null);

            if (customer == null ||
                    !passwordEncoder.matches(
                            password,
                            customer.getPassword())) {
                response.put("success", false);
                response.put("message",
                        "Invalid email or password!");
                return response;
            }

            String token = jwtUtil.generateToken(
                    customer.getEmail(), "CUSTOMER");
            DrivingLicense license = licenseRepository
                    .findByCustomerId(customer.getId())
                    .orElse(null);
            response.put("success", true);
            response.put("token", token);
            response.put("role", "CUSTOMER");
            response.put("name",
                    customer.getFirstname() + " " +
                            customer.getLastname());
            response.put("email", customer.getEmail());
            response.put("id", customer.getId());
            response.put("firstname",
                    customer.getFirstname());
            response.put("lastname",
                    customer.getLastname());
            response.put("contact",
                    customer.getContact());
            response.put("address",
                    customer.getAddress());
            response.put("licenseNumber",
                    license != null
                            ? license.getLicenseNumber()
                            : "");
            response.put("licenseExpiry",
                    license != null &&
                            license.getExpiryDate() != null
                            ? license.getExpiryDate().toString()
                            : "");
        }

        return response;
    }

    public Map<String, Object> getCustomerProfile(
            Long customerId) {
        Map<String, Object> response = new HashMap<>();

        Customer customer = customerRepository
                .findById(customerId)
                .orElse(null);

        if (customer == null) {
            response.put("success", false);
            response.put("message",
                    "Customer not found!");
            return response;
        }

        DrivingLicense license = licenseRepository
                .findByCustomerId(customerId)
                .orElse(null);

        response.put("success", true);
        response.put("id", customer.getId());
        response.put("firstname",
                customer.getFirstname());
        response.put("lastname",
                customer.getLastname());
        response.put("email", customer.getEmail());
        response.put("contact",
                customer.getContact());
        response.put("address",
                customer.getAddress());
        response.put("licenseNumber",
                license != null
                        ? license.getLicenseNumber()
                        : "");
        response.put("licenseExpiry",
                license != null &&
                        license.getExpiryDate() != null
                        ? license.getExpiryDate().toString()
                        : "");

        return response;
    }

    private String normalizeRole(String role) {
        if (role == null) {
            return "";
        }
        return role.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizePassword(String password) {
        if (password == null) {
            return "";
        }
        return password.trim();
    }

    private Admin findOrCreateDefaultAdmin(String email) {
        Optional<Admin> existingAdmin =
                adminRepository.findByEmail(email);

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            if (!isBcryptHash(admin.getPassword()) &&
                    defaultAdminEmail.equalsIgnoreCase(
                            admin.getEmail())) {
                admin.setName(defaultAdminName);
                admin.setPassword(passwordEncoder.encode(
                        defaultAdminPassword));
                admin.setRole("ADMIN");
                return adminRepository.save(admin);
            }
            return admin;
        }

        if (!defaultAdminEmail.equalsIgnoreCase(email)) {
            return null;
        }

        Admin admin = new Admin();
        admin.setName(defaultAdminName);
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode(
                defaultAdminPassword));
        admin.setRole("ADMIN");
        return adminRepository.save(admin);
    }

    private Admin ensureDefaultAdmin(String email) {
        Admin admin = findOrCreateDefaultAdmin(email);
        if (admin == null) {
            return null;
        }

        admin.setName(defaultAdminName);
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(passwordEncoder.encode(
                defaultAdminPassword));
        admin.setRole("ADMIN");
        return adminRepository.save(admin);
    }

    private boolean isDefaultAdminCredentials(
            String email, String password) {
        return defaultAdminEmail.equalsIgnoreCase(email) &&
                defaultAdminPassword.equals(password);
    }

    private boolean matchesPassword(
            String rawPassword,
            String storedPassword,
            String email) {
        if (storedPassword == null) {
            return false;
        }

        if (passwordEncoder.matches(rawPassword,
                storedPassword)) {
            return true;
        }

        return defaultAdminEmail.equalsIgnoreCase(email) &&
                defaultAdminPassword.equals(rawPassword);
    }

    private boolean isBcryptHash(String password) {
        return password != null &&
                password.matches("^\\$2[aby]\\$.{56}$");
    }
}
