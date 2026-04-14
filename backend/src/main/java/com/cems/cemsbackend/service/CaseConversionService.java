package com.cems.cemsbackend.service;

import org.springframework.stereotype.Service;

/**
 * Service utility to handle string case conversions for the application.
 */
@Service
public class CaseConversionService {

  /**
   * Converts a camelCase string to snake_case.
   *
   * @param input the string to convert
   * @return the converted snake_case string
   */
  public String camelToSnake(String input) {
    if (input == null || input.isEmpty()) {
      return input;
    }
    return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }
}
