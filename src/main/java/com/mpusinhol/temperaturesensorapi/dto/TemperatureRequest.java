package com.mpusinhol.temperaturesensorapi.dto;

import com.mpusinhol.temperaturesensorapi.validation.ValidTemperatureUnit;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public record TemperatureRequest(
        @NotNull
        Integer value,
        @ValidTemperatureUnit
        String unit,
        @NotNull
        Instant timestamp) {
}
