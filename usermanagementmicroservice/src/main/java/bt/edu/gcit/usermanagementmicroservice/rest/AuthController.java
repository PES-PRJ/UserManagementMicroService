package bt.edu.gcit.usermanagementmicroservice.rest;

import bt.edu.gcit.usermanagementmicroservice.config.JwtUtils;
import bt.edu.gcit.usermanagementmicroservice.dao.UserRepository;
import bt.edu.gcit.usermanagementmicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // 1. Authenticate the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // 2. Fetch user to get their role
        User user = userRepository.findByEmail(email);
        
        // 3. Generate the token
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole().getName());

        // 4. Return as JSON
        return Map.of("token", token);
    }

    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        User user = userRepository.findByRegistrationToken(token);

        if (user == null || user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Invalid or expired invitation link.");
        }

        // Encode and update password
        user.setPassword(passwordEncoder.encode(newPassword));
        // Clear token so it can't be used again
        user.setRegistrationToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password set successfully. You can now log in.");
    }
}