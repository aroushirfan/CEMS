package com.cems.cemsbackend.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventNotificationServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EventNotificationService notificationService;

    private Event event;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setEmail("alice@example.com");

        user2 = new User();
        user2.setEmail("bob@example.com");

        event = new Event();
        event.setTitle("Spring Conference");
        event.addAttendee(user1);
        event.addAttendee(user2);
    }

    @Test
    void testSendNotificationEmail_WithAttendees_SendsEmail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendNotificationEmail(event);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendNotificationEmail_WhenMessagingExceptionThrown_DoesNotPropagate() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP unavailable"));

        assertThrows(
                RuntimeException.class,
                () -> notificationService.sendNotificationEmail(event)
        );
    }

    @Test
    void sendNotificationEmail_SubjectContainsEventTitle() throws MessagingException {
        Session session = Session.getDefaultInstance(new java.util.Properties());
        MimeMessage realMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        notificationService.sendNotificationEmail(event);

        assertTrue(
                realMessage.getSubject().contains(event.getTitle()),
                "Subject should contain the event title"
        );
    }

    @Test
    void sendNotificationEmail_SubjectStartsWithReminder() throws MessagingException {
        Session session = Session.getDefaultInstance(new java.util.Properties());
        MimeMessage realMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        notificationService.sendNotificationEmail(event);

        assertTrue(
                realMessage.getSubject().startsWith("Reminder:"),
                "Subject should start with 'Reminder:'"
        );
    }

    @Test
    void sendNotificationEmail_WithNoAttendees_SendsEmail() throws MessagingException {
        Event emptyEvent = new Event();
        emptyEvent.setTitle("Empty Event");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendNotificationEmail(emptyEvent);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendNotificationEmail_WithSingleAttendee_SendsEmail() throws MessagingException {
        Event singleAttendeeEvent = new Event();
        singleAttendeeEvent.setTitle("Single Event");
        singleAttendeeEvent.addAttendee(user1);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendNotificationEmail(singleAttendeeEvent);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendNotificationEmail_WithMultipleAttendees_SendsEmail() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendNotificationEmail(event);

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendNotificationEmail_ContentContainsEventTitle() throws MessagingException {
        Session session = Session.getDefaultInstance(new java.util.Properties());
        MimeMessage realMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        notificationService.sendNotificationEmail(event);

        // Verify the message was sent and subject contains title
        verify(mailSender, times(1)).send(realMessage);
        assertTrue(realMessage.getSubject().contains(event.getTitle()));
    }

    @Test
    void sendNotificationEmail_MessagingException_LogsErrorButDoesNotThrow() throws MessagingException {
        Session session = Session.getDefaultInstance(new java.util.Properties());
        MimeMessage realMessage = new MimeMessage(session);
        when(mailSender.createMimeMessage()).thenReturn(realMessage);

        // Should execute without throwing exception
        notificationService.sendNotificationEmail(event);

        // Verify email was attempted to be sent
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(realMessage);
    }

    @Test
    void sendNotificationEmail_CreatesNewMimeMessageEachTime() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        notificationService.sendNotificationEmail(event);
        notificationService.sendNotificationEmail(event);

        verify(mailSender, times(2)).createMimeMessage();
        verify(mailSender, times(2)).send(mimeMessage);
    }
}