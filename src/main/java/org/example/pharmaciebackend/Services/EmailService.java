package org.example.pharmaciebackend.Services;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.example.pharmaciebackend.Dtos.ShiftResponse;
import org.example.pharmaciebackend.Entities.Shift;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendShiftNotification(String toEmail, String employeeName, Shift shift) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject("Nouveau service assigné - " + shift.getDate());

        String htmlContent =
                "<h2>Nouveau service assigné</h2>" +
                        "<p>Bonjour <b>" + employeeName + "</b>,</p>" +
                        "<p>Un nouveau service vous a été attribué :</p>" +
                        "<ul>" +
                        "<li><b>Date :</b> " + shift.getDate() + "</li>" +
                        "<li><b>Début :</b> " + shift.getStartTime() + "</li>" +
                        "<li><b>Fin :</b> " + shift.getEndTime() + "</li>" +
                        (shift.getDescription() != null ? "<li><b>Note :</b> " + shift.getDescription() + "</li>" : "") +
                        "</ul>" +
                        "<p>Merci,<br>Gestion Pharmacie</p>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
