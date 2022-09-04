package com.mpusinhol.temperaturesensorapi.fixture;

import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.model.TemperatureUnit;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TemperatureFixture {

    public static Temperature getTemperatureInput() {
        return Temperature.builder()
                .value(30)
                .unit(TemperatureUnit.CELSIUS)
                .timestamp(Instant.now())
                .build();
    }

    public static Temperature getTemperatureOutput() {
        return Temperature.builder()
                .id(1L)
                .value(30)
                .unit(TemperatureUnit.CELSIUS)
                .timestamp(Instant.now())
                .build();
    }

    public static List<Temperature> getTemperatureInputList(int numberOfItems) {
        List<Temperature> temperatures = new ArrayList<>();

        IntStream.range(0, numberOfItems)
                .forEach(i -> {
                    Temperature temperature = getTemperatureInput();
                    temperature.setTimestamp(temperature.getTimestamp().plus(i, ChronoUnit.SECONDS));
                    temperatures.add(temperature);
                });

        return temperatures;
    }

    public static List<Temperature> getTemperatureOutputList(List<Temperature> input) {
        List<Temperature> temperatures = new ArrayList<>();
        long id = 1;

        for (Temperature temperature : input) {
            temperatures.add(new Temperature(id++, temperature.getValue(), temperature.getUnit(), temperature.getTimestamp()));
        }

        return temperatures;
    }

    public static TemperatureRequest getTemperatureRequestObject() {
        return new TemperatureRequest(30, "CELSIUS", Instant.now());
    }

    public static TemperatureRequest getTemperatureRequestObject(Integer value, String unit, Instant timestamp) {
        return new TemperatureRequest(value, unit, timestamp);
    }

    public static List<TemperatureRequest> getTemperatureRequest(int numberOfItems) {
        List<TemperatureRequest> request = new ArrayList<>();

        IntStream.range(0, numberOfItems)
                .forEach(i -> {
                    TemperatureRequest temperatureRequest = getTemperatureRequestObject(30, "CELSIUS", Instant.now().plus(i, ChronoUnit.SECONDS));
                    request.add(temperatureRequest);
                });

        return request;
    }
}
