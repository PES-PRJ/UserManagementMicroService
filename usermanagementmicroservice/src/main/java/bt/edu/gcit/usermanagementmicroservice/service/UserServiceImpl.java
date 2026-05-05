package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public User save(User user) {
        // Generate a 24-hour token
        String token = UUID.randomUUID().toString();
        user.setRegistrationToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(24));
        
        User savedUser = userRepository.save(user);
        emailService.sendInvitationEmail(savedUser.getEmail(), token);
        return savedUser;
    }

    @Override
    public List<User> findAll() { return userRepository.findAll(); }

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
    public void deleteById(Long id) { userRepository.deleteById(id); }
}