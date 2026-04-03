package bt.edu.gcit.usermanagementmicroservice.utils;

import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.dao.RoleRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
import bt.edu.gcit.usermanagementmicroservice.entity.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo, RoleRepository roleRepo, BCryptPasswordEncoder encoder) {
        return args -> {
            // 1. Ensure Roles exist
            if (roleRepo.findByName("ADMIN") == null) {
                roleRepo.save(new Role("ADMIN"));
            }

            // 2. Look for the specific admin email
            User existingAdmin = userRepo.findByEmail("admin@gcit.edu.bt");

            if (existingAdmin == null) {
                User admin = new User();
                admin.setName("System Administrator");
                admin.setEmail("admin@gcit.edu.bt");
                admin.setRole("ADMIN");
                // Encoding is CRITICAL for AuthenticationManager to work
                admin.setPassword(encoder.encode("admin123"));

                userRepo.save(admin);
                System.out.println(">>> SEEDER: Admin account created: admin@gcit.edu.bt / admin123");
            } else {
                // Optional: Force update password to BCrypt if you've been getting 403s
                existingAdmin.setPassword(encoder.encode("admin123"));
                userRepo.save(existingAdmin);
                System.out.println(">>> SEEDER: Admin password updated to BCrypt.");
            }
        };
    }
}