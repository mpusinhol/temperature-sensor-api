package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.model.Temperature;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface TemperatureService {

    void create(Temperature temperature);

    @Async
    void create(List<Temperature> temperatures);

    Temperature findById(Long id);
}
