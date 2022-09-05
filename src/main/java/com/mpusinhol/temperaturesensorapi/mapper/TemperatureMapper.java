package com.mpusinhol.temperaturesensorapi.mapper;

import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

public class TemperatureMapper {

    public static Temperature toModel(TemperatureRequest temperatureRequest) {
        return Temperature.builder()
                .value(temperatureRequest.value())
                .unit(TemperatureUnit.valueOf(temperatureRequest.unit().toUpperCase()))
                .timestamp(temperatureRequest.timestamp())
                .build();
    }
}
