package com.cems.cemsbackend.repository;

import com.cems.cemsbackend.model.Event;
import com.cems.cemsbackend.model.EventTranslation;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing event translations.
 */
public interface EventTranslationRepository extends JpaRepository<EventTranslation, UUID> {

  /**
   * Retrieves a translation for a specific event and language.
   *
   * @param refEvent the parent event.
   * @param language the language code.
   * @return an optional containing the translation if found.
   */
  Optional<EventTranslation> getByRefEventAndLanguage(Event refEvent, String language);

  /**
   * Checks if a translation exists for a specific event and language.
   *
   * @param refEvent the parent event.
   * @param language the language code.
   * @return true if the translation exists, false otherwise.
   */
  boolean existsByRefEventAndLanguage(Event refEvent, String language);
}