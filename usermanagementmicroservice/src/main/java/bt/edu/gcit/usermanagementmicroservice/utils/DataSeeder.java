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
    CommandLineRunner initDatabase(UserRepository userRepo,
                                   RoleRepository roleRepo,
                                   BCryptPasswordEncoder encoder) {
        return args -> {

            // 1. Seed roles
            String[] roles = {"ADMIN", "EMPLOYEE", "ASSETMANAGER"};

            for (String roleName : roles) {
                if (roleRepo.findByName(roleName) == null) {
                    roleRepo.save(new Role(roleName));
                    System.out.println(">>> SEEDER: Role created: " + roleName);
                }
            }

            // 2. Get ADMIN role
            Role adminRole = roleRepo.findByName("ADMIN");

            // 3. Seed admin user
            User existingAdmin = userRepo.findByEmail("admin@gcit.edu.bt");

            if (existingAdmin == null) {
                User admin = new User();
                admin.setName("System Administrator");
                admin.setEmail("admin@gcit.edu.bt");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(adminRole);

                userRepo.save(admin);
                System.out.println(">>> SEEDER: Admin account created");
            } else {
                existingAdmin.setPassword(encoder.encode("admin123"));
                existingAdmin.setRole(adminRole);

                userRepo.save(existingAdmin);
                System.out.println(">>> SEEDER: Admin updated");
            }
        };
    }
}