package bt.edu.gcit.usermanagementmicroservice.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendInvitationEmail(String toEmail, String token) {
        String link = "http://localhost:3000/set-password?token=" + token;
        
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("Welcome to AMS - Set Your Password");

            String htmlContent = "<html>" +
                "<body>" +
                "<h2>Welcome to AMS</h2>" +
                "<p>An account has been created for you. Please click the button below to set your password and activate your account. <b>This link expires in 10 minutes.</b></p>" +
                "<a href=\"" + link + "\" style=\"" +
                "background-color: #6a89b5; " +
                "color: white; " +
                "padding: 12px 25px; " +
                "text-decoration: none; " +
                "border-radius: 5px; " +
                "display: inline-block; " +
                "font-weight: bold;\">" +
                "Set Password" +
                "</a>" +
                "<p>If the button doesn't work, copy and paste this link into your browser:</p>" +
                "<p>" + link + "</p>" +
                "</body>" +
                "</html>";

            helper.setText(htmlContent, true); // Set second parameter to true for HTML
            mailSender.send(message);
            
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}