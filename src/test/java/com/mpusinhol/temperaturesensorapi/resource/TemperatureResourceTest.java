package com.mpusinhol.temperaturesensorapi.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpusinhol.temperaturesensorapi.dto.StandardError;
import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
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

import java.util.List;

import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureOutput;
import static com.mpusinhol.temperaturesensorapi.fixture.TemperatureFixture.getTemperatureRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("Create temperature - single item")
    void createSingleItem() {
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
    @DisplayName("Create temperature - multiple items")
    void createBatched() {
        List<TemperatureRequest> request = getTemperatureRequest(10);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/temperatures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isAccepted());
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
