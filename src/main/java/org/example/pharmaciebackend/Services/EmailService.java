package org.example.pharmaciebackend.Services;



import org.example.pharmaciebackend.Dtos.ShiftResponse;
import org.example.pharmaciebackend.Entities.Shift;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendShiftNotification(String toEmail, String employeeName, Shift shift) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Shift Assigned - " + shift.getDate());
        message.setText(
                "Hello " + employeeName + ",\n\n" +
                        "A new shift has been assigned to you:\n" +
                        "Date     : " + shift.getDate() + "\n" +
                        "Start    : " + shift.getStartTime() + "\n" +
                        "End      : " + shift.getEndTime() + "\n" +
                        (shift.getDescription() != null ? "Note     : " + shift.getDescription() + "\n" : "") +
                        "\nPharmacy Management"
        );
        mailSender.send(message);
    }
}
