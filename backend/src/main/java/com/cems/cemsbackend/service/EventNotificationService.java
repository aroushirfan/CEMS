package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventNotificationService {
    private final JavaMailSender mailSender;

    public EventNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    /**
     * Send bulk reminder email for an event.
     * All registered users are BCC'd.
     */
    public void sendNotificationEmail(Event event) {
//        Get email of all registered users of the event
        List<User> registeredUsers = event.getAttendees();
        String[] attendeesEmails = registeredUsers.stream()
                .map(User::getEmail).toArray(String[]::new);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(System.getenv("admin_email"));
            helper.setBcc(attendeesEmails);
            helper.setSubject("Reminder: " + event.getTitle() + " is today!");
            helper.setText(buildEmailBody(event));

            mailSender.send(message);

        } catch (MessagingException e) {
            System.out.printf("Failed to send reminder for event '%s': %s",
                    event.getTitle(), e.getMessage());
        }
    }

    private String buildEmailBody(Event event) {
        return String.format("""
                Hello,
                Just a reminder that %s is happening today
                You're receiving this because you registered for this event.
                """,
                event.getTitle()
        );
    }
}