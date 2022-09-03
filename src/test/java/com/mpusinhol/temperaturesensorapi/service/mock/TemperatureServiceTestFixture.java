package com.mpusinhol.temperaturesensorapi.service.mock;

import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

import java.time.Instant;

public class TemperatureServiceTestFixture {

    public static Temperature getTemperatureInput() {
        return Temperature.builder()
                .value(30)
                .unit(TemperatureUnit.CELSIUS)
                .build();
    }

    public static Temperature getTemperatureOutput() {
        return Temperature.builder()
                .id(1L)
                .value(30)
                .unit(TemperatureUnit.CELSIUS)
                .createdAt(Instant.now())
                .build();
    }
}
