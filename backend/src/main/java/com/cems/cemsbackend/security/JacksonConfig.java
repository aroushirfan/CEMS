package com.cems.cemsbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to customize Jackson JSON serialization/deserialization.
 */
@Configuration
public class JacksonConfig {

  /**
   * Configures and provides the ObjectMapper bean with JavaTimeModule support.
   *
   * @return a configured ObjectMapper instance.
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .registerModule(new JavaTimeModule()); //  enables LocalDate
  }
}