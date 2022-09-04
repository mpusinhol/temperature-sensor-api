package com.mpusinhol.temperaturesensorapi.mapper;

import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.model.Temperature;

public class TemperatureMapper {

    public static Temperature toModel(TemperatureRequest temperatureRequest) {
        return Temperature.builder()
                .value(temperatureRequest.value())
                .unit(temperatureRequest.unit())
                .timestamp(temperatureRequest.timestamp())
                .build();
    }
}
