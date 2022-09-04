package com.mpusinhol.temperaturesensorapi.service.impl;

import com.mpusinhol.temperaturesensorapi.exception.DuplicatedTemperatureException;
import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.repository.TemperatureRepository;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureRepository temperatureRepository;

    public void create(Temperature temperature) {
        temperature.setId(null);

        try {
            Temperature newTemperature = temperatureRepository.save(temperature);
            temperature.setId(newTemperature.getId());
        } catch (DataIntegrityViolationException e) {
            String message = String.format("%s already exists.", temperature);
            throw new DuplicatedTemperatureException(message, e);
        }
    }

    public void create(List<Temperature> temperatures) {
        for (Temperature temperature : temperatures) {
            try {
                create(temperature);
            } catch (Exception e) {
                //Still want to process the others even if one fails
                log.error("Could not create temperature {}.", temperature, e);
            }
        }
    }

    public Temperature findById(Long id) {
        Optional<Temperature> temperature = temperatureRepository.findById(id);

        return temperature.orElseThrow(() -> new ObjectNotFoundException(String.format("Temperature with id %d not found.", id)));
    }
}
