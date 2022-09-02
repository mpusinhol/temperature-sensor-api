package com.mpusinhol.temperaturesensorapi.mapper;

import com.mpusinhol.temperaturesensorapi.dto.Temperature;

public class TemperatureMapper {

    public static Temperature toDTO(com.mpusinhol.temperaturesensorapi.model.Temperature temperature) {
        return new Temperature(
                temperature.getId(),
                temperature.getValue(),
                temperature.getUnit(),
                temperature.getCreatedAt()
        );
    }

    public static com.mpusinhol.temperaturesensorapi.model.Temperature toModel(Temperature temperature) {
        return com.mpusinhol.temperaturesensorapi.model.Temperature.builder()
                .value(temperature.value())
                .unit(temperature.unit())
                .build();
    }
}
