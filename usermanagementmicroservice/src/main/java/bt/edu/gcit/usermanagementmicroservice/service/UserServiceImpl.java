package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.dao.RoleRepository;
import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
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
    // public User save(User user) {
    // // Generate a 24-hour token
    // String token = UUID.randomUUID().toString();
    // user.setRegistrationToken(token);
    // user.setTokenExpiry(LocalDateTime.now().plusHours(24));

    // User savedUser = userRepository.save(user);
    // emailService.sendInvitationEmail(savedUser.getEmail(), token);
    // return savedUser;
    // }

    public User save(User user) {
        // Fetch the managed role from the DB first[cite: 3, 8]
        Role existingRole = roleRepository.findByName(user.getRole().getName());
        if (existingRole == null) {
            throw new RuntimeException("Role not found");
        }
        user.setRole(existingRole); // Link the user to the existing DB role[cite: 4]

        // Generate token logic[cite: 9]
        String token = UUID.randomUUID().toString();
        user.setRegistrationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));

        return userRepository.save(user); // [cite: 2, 9]
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(Long id, User user) {
        User existing = userRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setName(user.getName());
            existing.setRole(user.getRole());
            return userRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}