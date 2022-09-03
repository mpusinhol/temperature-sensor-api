package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.repository.TemperatureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Optional;

import static com.mpusinhol.temperaturesensorapi.service.mock.TemperatureServiceTestFixture.getTemperatureInput;
import static com.mpusinhol.temperaturesensorapi.service.mock.TemperatureServiceTestFixture.getTemperatureOutput;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TemperatureServiceTest {

    @MockBean
    private TemperatureRepository temperatureRepository;

    @Autowired
    private TemperatureService temperatureService;

    @Test
    @DisplayName("Create temperature - happy flow")
    void createHappyFlow() {
        Temperature input = getTemperatureInput();
        Temperature output = getTemperatureOutput();

        when(temperatureRepository.save(input)).thenReturn(output);

        Temperature temperature = temperatureService.create(input);

        assertEquals(output, temperature);
    }

    @Test
    @DisplayName("Create temperature - previously set id and creation timestamp")
    void createWithPreviousIdAndTimestamp() {
        Temperature input = getTemperatureInput();
        Temperature output = getTemperatureOutput();

        input.setId(2L);
        input.setCreatedAt(Instant.MIN);

        when(temperatureRepository.save(input)).thenReturn(output);

        Temperature temperature = temperatureService.create(input);

        assertEquals(output, temperature);
    }

    @Test
    @DisplayName("Find temperature by id - happy flow")
    void findByIdHappyFlow() {
        Temperature output = getTemperatureOutput();

        when(temperatureRepository.findById(1L)).thenReturn(Optional.of(output));

        Temperature temperature = temperatureService.findById(1L);

        assertEquals(output, temperature);
    }

    @Test
    @DisplayName("Find temperature by id - not found")
    void findByIdNotFound() {
        when(temperatureRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> temperatureService.findById(1L),
                "Temperature with id 1 not found.");
    }
}
