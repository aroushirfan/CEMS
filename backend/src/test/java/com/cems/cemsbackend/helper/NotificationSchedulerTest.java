package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.service.EventNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationSchedulerTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventNotificationService notificationService;

    @InjectMocks
    private NotificationScheduler scheduler;

    private Event todayEvent;
    private Event futureEvent;

    @BeforeEach
    void setUp() {
        Instant todayInstant = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        todayEvent = new Event();
        todayEvent.setTitle("Today's Event");
        todayEvent.setDateTime(todayInstant);

        // Event scheduled for tomorrow
        Instant tomorrowInstant = LocalDate.now()
                .plusDays(1)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        futureEvent = new Event();
        futureEvent.setTitle("Future Event");
        futureEvent.setDateTime(tomorrowInstant);
    }

    @Test
    void testSendDayOfEvent_WithTodayEvents_SendsNotifications() {
        when(eventRepository.findAll()).thenReturn(List.of(todayEvent, futureEvent));
        scheduler.sendDayOfEvent();

        // Only the today event should trigger a notification
        verify(notificationService, times(1)).sendNotificationEmail(todayEvent);
        verify(notificationService, never()).sendNotificationEmail(futureEvent);
    }

    @Test
    void testSendDayOfEvent_WithNoEvents_SendsNoNotifications() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());
        scheduler.sendDayOfEvent();
        verify(notificationService, never()).sendNotificationEmail(any());
    }

    @Test
    void testSendDayOfEvent_WithOnlyFutureEvents_SendsNoNotifications() {
        when(eventRepository.findAll()).thenReturn(List.of(futureEvent));
        scheduler.sendDayOfEvent();
        verify(notificationService, never()).sendNotificationEmail(any());
    }

    @Test
    void testSendDayOfEvent_WithMultipleTodayEvents_SendsNotificationForEach() {
        Event anotherTodayEvent = new Event();
        anotherTodayEvent.setTitle("Another Today Event");
        anotherTodayEvent.setDateTime(
                LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
        );

        when(eventRepository.findAll()).thenReturn(List.of(todayEvent, anotherTodayEvent));

        scheduler.sendDayOfEvent();

        verify(notificationService, times(1)).sendNotificationEmail(todayEvent);
        verify(notificationService, times(1)).sendNotificationEmail(anotherTodayEvent);
    }

    @Test
    void testSendDayOfEvent_CallsFindAllExactlyOnce() {
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        scheduler.sendDayOfEvent();

        verify(eventRepository, times(1)).findAll();
    }
}