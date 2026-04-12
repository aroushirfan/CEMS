package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventTranslationRepository extends JpaRepository<EventTranslation, UUID> {
    Optional<EventTranslation> getByRefEventAndLanguage(Event refEvent, String language);
    boolean existsByRefEventAndLanguage(Event refEvent, String language);
}
