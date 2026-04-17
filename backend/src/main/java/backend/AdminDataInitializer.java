package backend;

import com.carrental.model.Admin;
import com.carrental.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminDataInitializer implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(AdminDataInitializer.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-name:Admin User}")
    private String defaultAdminName;

    @Value("${app.admin.default-email:admin@carrental.com}")
    private String defaultAdminEmail;

    @Value("${app.admin.default-password:Admin@123}")
    private String defaultAdminPassword;

    public AdminDataInitializer(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Optional<Admin> existingAdmin =
                adminRepository.findByEmail(defaultAdminEmail);

        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();

            // Repair older setups where the admin password
            // may have been inserted as plain text.
            if (!isBcryptHash(admin.getPassword())) {
                admin.setName(defaultAdminName);
                admin.setPassword(
                        passwordEncoder.encode(defaultAdminPassword));
                admin.setRole("ADMIN");
                adminRepository.save(admin);
                logger.info(
                        "Default admin password repaired for email: {}",
                        defaultAdminEmail);
                return;
            }

            logger.info("Admin user already exists for email: {}",
                    defaultAdminEmail);
            return;
        }

        Admin admin = new Admin();
        admin.setName(defaultAdminName);
        admin.setEmail(defaultAdminEmail);
        admin.setPassword(
                passwordEncoder.encode(defaultAdminPassword));
        admin.setRole("ADMIN");
        adminRepository.save(admin);

        logger.info("Default admin user created for email: {}",
                defaultAdminEmail);
    }

    private boolean isBcryptHash(String password) {
        return password != null &&
                password.matches("^\\$2[aby]\\$.{56}$");
    }
}
