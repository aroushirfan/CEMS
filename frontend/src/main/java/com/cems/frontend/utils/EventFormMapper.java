package com.cems.frontend.utils;

import com.cems.shared.model.EventDto.EventRequestDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * A utility class to map event form to EventRequestDTO
 */
public class EventFormMapper {

    private EventFormMapper() {}

    /**
     * Create EventRequestDTO from raw form data
     * @return {@link EventRequestDTO}
     */
    public static EventRequestDTO map(LocalDate date, int hour, int minute, String title, String description, String location, long capacity) {
        if (date == null) {
            throw new IllegalArgumentException("Date is required");
        }


//        int hour = Integer.parseInt(hourComboBox.getValue());
//        int minute = Integer.parseInt(minuteComboBox.getValue());

        Instant eventInstant = date
                .atTime(hour, minute)
                .atZone(ZoneId.systemDefault())
                .toInstant();

//        titleField.getText(),
//                descriptionField.getText(),
//                locationField.getText(),
//                Long.parseLong(capacityField.getText()),

        return new EventRequestDTO(
                title,
                description,
                location,
                capacity,
                eventInstant
        );
    }
}
