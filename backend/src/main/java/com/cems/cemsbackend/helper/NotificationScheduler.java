package com.cems.cemsbackend.helper;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.repository.EventRepository;
import com.cems.cemsbackend.service.EventNotificationService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler component that automates event-related notifications.
 */
@Component
public class NotificationScheduler {
  private final EventRepository eventRepository;
  private final EventNotificationService notificationService;

  /**
   * Constructor for NotificationScheduler.
   *
   * @param eventRepository     repository to fetch event data.
   * @param notificationService service to handle email/notification logic.
   */
  public NotificationScheduler(
      EventRepository eventRepository,
      EventNotificationService notificationService) {
    this.eventRepository = eventRepository;
    this.notificationService = notificationService;
  }

  /**
   * Runs every day at 7:00 AM (server time).
   * Finds all events scheduled for today and emails every registered user.
   */
  @Transactional
  @Scheduled(cron = "0 0 7 * * *")
  public void sendDayOfEvent() {
    LocalDate today = LocalDate.now();

    List<Event> todayEvents = eventRepository.findAll().stream()
        .filter(event ->
            event.getDateTime().atZone(ZoneId.systemDefault()).toLocalDate()
                .equals(LocalDate.now())).toList();

    if (todayEvents.isEmpty()) {
      System.out.println("No events today — nothing to send.");
      return;
    }
    for (Event event : todayEvents) {
      notificationService.sendNotificationEmail(event);
    }

    System.out.printf("Notification job complete. Processed %s event(s).",
        todayEvents.size());
  }
}