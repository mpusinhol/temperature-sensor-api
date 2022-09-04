package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Map;

public interface TemperatureService {

    void create(Temperature temperature);

    @Async
    void create(List<Temperature> temperatures);

    Map<String, List<Temperature>> findAll(AggregationMode aggregationMode);

    Temperature findById(Long id);
}
