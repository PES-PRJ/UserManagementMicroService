package bt.edu.gcit.usermanagementmicroservice.service;

import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final String SECRET_KEY = "YourSuperSecretKeyForBhutanProject"; // Move to environment variables!

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email); // Ensure findByEmail is in UserRepository
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Generate JWT including the Role (e.g., ROLE_ADMIN)
            return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole()) 
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        }
        throw new RuntimeException("Invalid credentials");
    }
}