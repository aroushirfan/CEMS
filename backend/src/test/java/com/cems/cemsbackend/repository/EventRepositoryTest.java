package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles; // Add this
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void should_find_no_events_if_repository_is_empty() {
        // Clear anything that might be there (just in case)
        eventRepository.deleteAll();

        List<Event> events = eventRepository.findAll();
        assertThat(events).isEmpty();
    }
}