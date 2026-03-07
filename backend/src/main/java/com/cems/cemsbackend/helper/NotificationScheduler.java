package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.service.EventNotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class NotificationScheduler {
    private final EventRepository eventRepository;
    private final EventNotificationService notificationService;

    public NotificationScheduler(EventRepository eventRepository,
                                      EventNotificationService notificationService) {
        this.eventRepository = eventRepository;
        this.notificationService = notificationService;
    }

    /**
     * Runs every day at 8:00 AM (server time).
     * Finds all events scheduled for today and emails every registered user.
     *
     * Cron format: second minute hour day-of-month month day-of-week
     */
    @Scheduled(cron = "${app.notification.cron:0 0 8 * * *}")
    public void sendDayOfReminders() {
        LocalDate today = LocalDate.now();

        List<Event> todayEvents = eventRepository.findAll().stream()
                .filter(event ->
                        event.getDateTime().truncatedTo(ChronoUnit.DAYS)
                                .equals(today.atStartOfDay(ZoneOffset.UTC).toInstant()))
                .toList();

        if (todayEvents.isEmpty()) {
            System.out.println("No events today — nothing to send.");
            return;
        }

        for (Event event : todayEvents) {
            notificationService.sendReminderEmail(event);
        }

        System.out.printf("Notification job complete. Processed %s event(s).", todayEvents.size());
    }
}