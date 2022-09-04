package com.mpusinhol.temperaturesensorapi.dto;

import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;

public enum AggregationMode {
    NONE,
    HOURLY,
    DAILY;

    public static final String MESSAGE = "Temperature unit is mandatory and must be one of " + Arrays.toString(TemperatureUnit.values());

    public static String extractReferenceFromTimestamp(Instant timestamp, AggregationMode aggregationMode) {
        return switch (aggregationMode) {
            case DAILY -> LocalDate.ofInstant(timestamp, ZoneId.of("UTC")).toString();
            case HOURLY -> LocalDateTime.ofInstant(timestamp, ZoneId.of("UTC"))
                    .withMinute(0)
                    .withSecond(0)
                    .withNano(0)
                    .toString();
            default -> "Temperatures";
        };
    }
}
