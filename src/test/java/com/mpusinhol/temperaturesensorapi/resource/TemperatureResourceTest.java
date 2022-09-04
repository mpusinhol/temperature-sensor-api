package com.mpusinhol.temperaturesensorapi.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
import com.mpusinhol.temperaturesensorapi.dto.StandardError;
import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.exception.DuplicatedTemperatureException;
import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getFindAllDailyAggregationResponse;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getFindAllNoAggregationResponse;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureInput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@WebMvcTest(TemperatureResource.class)
@AutoConfigureMockMvc
public class TemperatureResourceTest {

    @MockBean
    private TemperatureService temperatureService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    @DisplayName("Create temperature - single item - happy flow")
    void createSingleItemHappyFlow() {
        List<TemperatureRequest> request = getTemperatureRequest(1);

        String response = mockMvc.perform(
                MockMvcRequestBuilders.post("/temperatures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        assertNotNull(response);
        assertTrue(response.contains("/temperatures/"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Create temperature - single item - duplicated")
    void createSingleItemDuplicated() {
        Temperature input = getTemperatureInput();
        List<TemperatureRequest> request = getTemperatureRequest(1);
        String message = String.format("%s already exists.", input);

        doThrow(new DuplicatedTemperatureException(message)).when(temperatureService).create(any(Temperature.class));

        mockMvc.perform(
                MockMvcRequestBuilders.post("/temperatures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @SneakyThrows
    @DisplayName("Create temperature - multiple items - happy flow")
    void createBatchedHappyFlow() {
        List<TemperatureRequest> request = getTemperatureRequest(10);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/temperatures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @SneakyThrows
    @DisplayName("Create temperature - multiple items - with exception in between")
    void createBatchedWithException() {
        List<TemperatureRequest> request = getTemperatureRequest(3);
        Temperature temperature = Temperature.builder()
                .value(request.get(1).value())
                .unit(request.get(1).unit())
                .timestamp(request.get(1).timestamp())
                .build();
        String message = String.format("%s already exists.", temperature);

        doThrow(new DuplicatedTemperatureException(message)).when(temperatureService).create(temperature);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/temperatures")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    @SneakyThrows
    @DisplayName("Find all temperatures - no aggregation")
    void findAllNoAggregation() {
        int numberOfItems = 5;
        Map<String, List<Temperature>> output = getFindAllNoAggregationResponse(numberOfItems);

        when(temperatureService.findAll(AggregationMode.NONE)).thenReturn(output);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        Map<String, List<Temperature>> temperatures = objectMapper.readValue(response, new TypeReference<>(){});

        assertEquals(1, temperatures.size());
        assertEquals("Temperatures", temperatures.keySet().stream().findFirst().get());
        assertEquals(numberOfItems, temperatures.get("Temperatures").size());
    }

    @Test
    @SneakyThrows
    @DisplayName("Find all temperatures - daily aggregation")
    void findAllDailyAggregation() {
        int numberOfItems = 5;
        List<String> days = List.of("2022-09-05", "2022-09-04");
        Map<String, List<Temperature>> output = getFindAllDailyAggregationResponse(days, numberOfItems);

        when(temperatureService.findAll(AggregationMode.DAILY)).thenReturn(output);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures?aggregate=DAILY"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        LinkedHashMap<String, List<Temperature>> temperatures = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(2, temperatures.size());
        assertTrue(days.stream().allMatch(temperatures::containsKey));
        assertEquals(new HashSet<>(days), temperatures.keySet());
        assertTrue(temperatures.values().stream().allMatch(list -> list.size() == numberOfItems));
    }

    @Test
    @SneakyThrows
    @DisplayName("Find all temperatures - hourly aggregation")
    void findAllHourlyAggregation() {
        int numberOfItems = 5;
        List<String> hours = List.of("2022-09-05T10:00", "2022-09-05T09:00");
        Map<String, List<Temperature>> output = getFindAllDailyAggregationResponse(hours, numberOfItems);

        when(temperatureService.findAll(AggregationMode.HOURLY)).thenReturn(output);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures?aggregate=HOURLY"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(response);

        LinkedHashMap<String, List<Temperature>> temperatures = objectMapper.readValue(response, new TypeReference<>() {});

        assertEquals(2, temperatures.size());
        assertTrue(hours.stream().allMatch(temperatures::containsKey));
        assertEquals(new HashSet<>(hours), temperatures.keySet());
        assertTrue(temperatures.values().stream().allMatch(list -> list.size() == numberOfItems));
    }

    @Test
    @SneakyThrows
    @DisplayName("Find temperature by id - happy flow")
    void findByIdHappyFlow() {
        Temperature output = getTemperatureOutput();

        when(temperatureService.findById(1L)).thenReturn(output);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Temperature temperature = objectMapper.readValue(response, Temperature.class);

        assertEquals(output, temperature);
    }

    @Test
    @SneakyThrows
    @DisplayName("Find temperature by id - not found")
    void findByIdNotFound() {
        String errorMessage = "Temperature with id %d not found.";

        when(temperatureService.findById(1L)).thenThrow(new ObjectNotFoundException(errorMessage));

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/temperatures/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        StandardError standardError = objectMapper.readValue(response, StandardError.class);

        assertEquals(errorMessage, standardError.message());
    }
}
