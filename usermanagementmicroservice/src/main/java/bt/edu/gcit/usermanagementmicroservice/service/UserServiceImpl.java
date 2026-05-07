package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.dao.RoleRepository;
import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
import bt.edu.gcit.usermanagementmicroservice.utils.EmailService;
import bt.edu.gcit.usermanagementmicroservice.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public User save(User user) {
        // 1. Fetch the managed role from the DB first to avoid detached entity issues
        Role existingRole = roleRepository.findByName(user.getRole().getName());
        if (existingRole == null) {
            throw new RuntimeException("Role not found");
        }
        user.setRole(existingRole); 

        // 2. Generate invitation token logic
        String token = UUID.randomUUID().toString();
        user.setRegistrationToken(token);
        
        // 3. Set expiry for exactly 10 minutes from now
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(10));

        // 4. Save user initially without a password
        User savedUser = userRepository.save(user);

        // 5. Explicitly trigger the email service
        emailService.sendInvitationEmail(savedUser.getEmail(), token);

        return savedUser; 
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User userDetails) {
        // 1. Find existing user
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // 2. Update only allowed fields (Name and Email)
        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());

        // 3. Update Role if provided in request
        if (userDetails.getRole() != null && userDetails.getRole().getName() != null) {
            Role updatedRole = roleRepository.findByName(userDetails.getRole().getName());
            if (updatedRole != null) {
                existingUser.setRole(updatedRole);
            }
        }

        // Save and return
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}