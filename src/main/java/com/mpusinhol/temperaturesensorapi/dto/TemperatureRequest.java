package com.mpusinhol.temperaturesensorapi.dto;

import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;
import com.mpusinhol.temperaturesensorapi.validation.ValidTemperatureUnit;

import javax.validation.constraints.NotNull;
import java.time.Instant;

public record TemperatureRequest(
        @NotNull
        Integer value,
        @ValidTemperatureUnit
        TemperatureUnit unit,
        @NotNull
        Instant timestamp) {
}
