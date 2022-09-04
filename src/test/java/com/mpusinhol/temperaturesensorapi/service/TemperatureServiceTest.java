package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
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

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureInput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureInputList;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutputList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @DisplayName("Find all temperatures - no aggregation")
    void findAllNoAggregation() {
        List<Temperature> input = getTemperatureInputList(10);
        List<Temperature> output = getTemperatureOutputList(input);

        when(temperatureRepository.findAll()).thenReturn(output);

        Map<String, List<Temperature>> response = temperatureService.findAll(AggregationMode.NONE);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Temperatures", response.keySet().stream().findFirst().get());
        assertEquals(output, response.get("Temperatures"));
        assertEquals(output.stream().sorted(Comparator.comparing(Temperature::getTimestamp)).toList(), response.get("Temperatures"));
    }

    @Test
    @DisplayName("Find all temperatures - daily aggregation")
    void findAllDailyAggregation() {
        List<String> days = List.of("2022-09-04", "2022-09-05");
        List<Instant> firstDayTimestamps = List.of(Instant.parse("2022-09-04T11:31:41Z"), Instant.parse("2022-09-04T11:31:40Z"));
        List<Instant> secondDayTimestamps = List.of(Instant.parse("2022-09-05T11:31:41Z"), Instant.parse("2022-09-05T10:31:41Z"));
        List<Temperature> input = getTemperatureInputList(Stream.concat(firstDayTimestamps.stream(), secondDayTimestamps.stream()).toList());
        List<Temperature> output = getTemperatureOutputList(input);

        when(temperatureRepository.findAll()).thenReturn(output);

        Map<String, List<Temperature>> response = temperatureService.findAll(AggregationMode.DAILY);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(days, response.keySet().stream().toList());

        assertNotNull(response.get(days.get(0)));
        assertEquals(2, response.get(days.get(0)).size());
        Temperature temperature1 = response.get(days.get(0)).get(0);
        Temperature temperature2 = response.get(days.get(0)).get(1);
        assertEquals(-1, temperature1.getTimestamp().compareTo(temperature2.getTimestamp()));

        assertNotNull(response.get(days.get(1)));
        assertEquals(2, response.get(days.get(1)).size());
        temperature1 = response.get(days.get(1)).get(0);
        temperature2 = response.get(days.get(1)).get(1);
        assertEquals(-1, temperature1.getTimestamp().compareTo(temperature2.getTimestamp()));
    }

    @Test
    @DisplayName("Find all temperatures - hourly aggregation")
    void findAllHourlyAggregation() {
        List<String> hours = List.of("2022-09-04T10:00", "2022-09-04T11:00", "2022-09-05T10:00", "2022-09-05T11:00");
        List<Instant> firstDayTimestamps = List.of(
                Instant.parse("2022-09-04T11:31:41Z"),
                Instant.parse("2022-09-04T11:31:40Z"),
                Instant.parse("2022-09-04T10:31:40Z"));
        List<Instant> secondDayTimestamps = List.of(
                Instant.parse("2022-09-05T11:31:41Z"),
                Instant.parse("2022-09-05T10:31:41Z"),
                Instant.parse("2022-09-05T10:31:40Z"));
        List<Temperature> input = getTemperatureInputList(Stream.concat(firstDayTimestamps.stream(), secondDayTimestamps.stream()).toList());
        List<Temperature> output = getTemperatureOutputList(input);

        when(temperatureRepository.findAll()).thenReturn(output);

        Map<String, List<Temperature>> response = temperatureService.findAll(AggregationMode.HOURLY);

        assertNotNull(response);
        assertEquals(4, response.size());
        assertEquals(hours, response.keySet().stream().toList());

        assertNotNull(response.get(hours.get(0)));
        assertEquals(1, response.get(hours.get(0)).size());

        assertNotNull(response.get(hours.get(1)));
        assertEquals(2, response.get(hours.get(1)).size());
        Temperature temperature1 = response.get(hours.get(1)).get(0);
        Temperature temperature2 = response.get(hours.get(1)).get(1);
        assertEquals(-1, temperature1.getTimestamp().compareTo(temperature2.getTimestamp()));

        assertNotNull(response.get(hours.get(2)));
        assertEquals(2, response.get(hours.get(2)).size());
        temperature1 = response.get(hours.get(2)).get(0);
        temperature2 = response.get(hours.get(2)).get(1);
        assertEquals(-1, temperature1.getTimestamp().compareTo(temperature2.getTimestamp()));

        assertNotNull(response.get(hours.get(3)));
        assertEquals(1, response.get(hours.get(3)).size());
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
