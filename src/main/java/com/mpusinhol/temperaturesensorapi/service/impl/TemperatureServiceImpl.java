package com.mpusinhol.temperaturesensorapi.service.impl;

import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.repository.TemperatureRepository;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureRepository temperatureRepository;

    public Temperature create(Temperature temperature) {
        temperature.setId(null);
        temperature.setCreatedAt(null);

        return temperatureRepository.save(temperature);
    }

    public Temperature findById(Long id) {
        Optional<Temperature> temperature = temperatureRepository.findById(id);

        return temperature.orElseThrow(() -> new ObjectNotFoundException(String.format("Temperature with id %d not found.", id)));
    }
}
