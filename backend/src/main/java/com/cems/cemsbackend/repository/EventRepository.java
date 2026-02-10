package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    Optional<Event> getEventById(UUID id);
    Optional<Event> deleteEventById(UUID id);
}
