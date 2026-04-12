package com.cems.cemsbackend.service;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Service for sending email notifications for events.
 */
@Service
public class EventNotificationService {

  /** Logger for tracking email transmission status. */
  private static final Logger LOGGER = LoggerFactory.getLogger(EventNotificationService.class);

  /** Sender used to transmit email messages. */
  private final JavaMailSender mailSender;

  /**
   * Constructs the notification service.
   *
   * @param mailSender the mail sender implementation
   */
  public EventNotificationService(final JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * Send bulk reminder email for an event. All registered users are BCC'd.
   *
   * @param event the event to notify attendees about
   */
  public void sendNotificationEmail(final Event event) {
    final List<User> registeredUsers = event.getAttendees();
    final String[] attendeesEmails = registeredUsers.stream()
            .map(User::getEmail).toArray(String[]::new);
    try {
      final MimeMessage message = mailSender.createMimeMessage();
      final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setTo(System.getenv().getOrDefault("admin_email", "test@gmail.com"));
      helper.setBcc(attendeesEmails);
      helper.setSubject("Reminder: " + event.getTitle() + " is today!");
      helper.setText(buildEmailBody(event));

      mailSender.send(message);

    } catch (MessagingException e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error("Failed to send reminder for event '{}': {}",
                event.getTitle(), e.getMessage());
      }
    }
  }

  /**
   * Builds the plain-text body of the reminder email.
   *
   * @param event the event context
   * @return formatted string body
   */
  private String buildEmailBody(final Event event) {
    return String.format(
            """
            Hello,
            Just a reminder that %s is happening today
            You're receiving this because you registered for this event.
            """,
            event.getTitle()
    );
  }
}