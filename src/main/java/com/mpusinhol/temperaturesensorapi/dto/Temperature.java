package com.mpusinhol.temperaturesensorapi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Temperature(
        Long id,
        Integer value,
        TemperatureUnit unit,
        Instant createdAt) {
}
