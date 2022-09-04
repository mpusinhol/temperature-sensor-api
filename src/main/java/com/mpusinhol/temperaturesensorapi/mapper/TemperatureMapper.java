package com.mpusinhol.temperaturesensorapi.mapper;

import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

public class TemperatureMapper {

    public static com.mpusinhol.temperaturesensorapi.dto.Temperature toDTO(Temperature temperature) {
        return new com.mpusinhol.temperaturesensorapi.dto.Temperature(
                temperature.getId(),
                temperature.getValue(),
                temperature.getUnit(),
                temperature.getTimestamp()
        );
    }

    public static Temperature toModel(com.mpusinhol.temperaturesensorapi.dto.Temperature temperature) {
        return Temperature.builder()
                .value(temperature.value())
                .unit(temperature.unit())
                .timestamp(temperature.timestamp())
                .build();
    }

    public static Temperature toModel(TemperatureRequest temperatureRequest) {
        return Temperature.builder()
                .value(temperatureRequest.value())
                .unit(TemperatureUnit.valueOf(temperatureRequest.unit().toUpperCase()))
                .timestamp(temperatureRequest.timestamp())
                .build();
    }
}
