package com.cems.cemsbackend.service;

import org.springframework.stereotype.Service;

@Service
public class CaseConversionService {

    public String camelToSnake(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
