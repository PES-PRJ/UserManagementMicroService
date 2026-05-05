package bt.edu.gcit.usermanagementmicroservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvitationEmail(String toEmail, String token) {
        // Link points to your React/Frontend URL
        String link = "http://localhost:3000/set-password?token=" + token;
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Welcome to AMS - Set Your Password");
        message.setText("An account has been created for you. Click the link to set your password: " + link);
        
        mailSender.send(message);
    }
}