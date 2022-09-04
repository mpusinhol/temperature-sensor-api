package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.exception.DuplicatedTemperatureException;
import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.repository.TemperatureRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureInput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureInputList;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutputList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TemperatureServiceTest {

    @MockBean
    private TemperatureRepository temperatureRepository;

    @Autowired
    private TemperatureService temperatureService;

    @Test
    @DisplayName("Create temperature - single item - happy flow")
    void createSingleItemHappyFlow() {
        Temperature input = getTemperatureInput();
        Temperature output = getTemperatureOutput();

        assertNull(input.getId());

        when(temperatureRepository.save(input)).thenReturn(output);

        temperatureService.create(input);

        assertEquals(1L, input.getId());
    }

    @Test
    @DisplayName("Create temperature - single item - previously set id")
    void createSingleItemWithPreviousId() {
        Temperature input = getTemperatureInput();
        Temperature output = getTemperatureOutput();

        input.setId(2L);

        when(temperatureRepository.save(input)).thenReturn(output);

        temperatureService.create(input);

        assertEquals(1L, input.getId());
    }

    @Test
    @DisplayName("Create temperature - single item - duplicated temperature")
    void createSingleItemDuplicatedTemperature() {
        Temperature input = getTemperatureInput();

        when(temperatureRepository.save(input)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DuplicatedTemperatureException.class,
                () -> temperatureService.create(input),
                String.format("%s already exists", input));
    }

    @Test
    @DisplayName("Create temperature - multiple items - happy flow")
    void createMultipleItemsHappyFlow() {
        int numberOfItems = 3;
        List<Temperature> input = getTemperatureInputList(numberOfItems);
        List<Temperature> output = getTemperatureOutputList(input);

        assertTrue(input.stream().allMatch(temperature -> Objects.isNull(temperature.getId())));

        IntStream.range(0, 3)
                .forEach(i -> when(temperatureRepository.save(input.get(i))).thenReturn(output.get(i)));

        temperatureService.create(input);

        verify(temperatureRepository, times(numberOfItems)).save(any());
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
