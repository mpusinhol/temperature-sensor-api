package com.mpusinhol.temperaturesensorapi.service.impl;

import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
import com.mpusinhol.temperaturesensorapi.exception.DuplicatedTemperatureException;
import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.repository.TemperatureRepository;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.mpusinhol.temperaturesensorapi.dto.AggregationMode.extractReferenceFromTimestamp;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemperatureServiceImpl implements TemperatureService {

    private final TemperatureRepository temperatureRepository;

    @Override
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

    @Override
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

    /*
        Considered using an inner TreeSet, however ended up with a simple list
        as I think that resorting the collection everytime a temperature is added
        would be less performant than simply sorting everything at the end of the process
    */
    @Override
    public Map<String, List<Temperature>> findAll(AggregationMode aggregationMode) {
        Map<String, List<Temperature>> aggregatedTemperatures = new HashMap<>();

        temperatureRepository.findAll()
                .forEach(temperature -> {
                    String reference = extractReferenceFromTimestamp(temperature.getTimestamp(), aggregationMode);

                    aggregatedTemperatures.computeIfPresent(reference, (key, value) ->
                        Stream.concat(value.stream(), Stream.of(temperature)).toList()
                    );

                    aggregatedTemperatures.computeIfAbsent(reference, key -> List.of(temperature));
                });

        aggregatedTemperatures.entrySet()
                .parallelStream()
                .forEach(entry -> {
                    List<Temperature> temperatures = entry.getValue()
                            .stream()
                            .sorted(Comparator.comparing(Temperature::getTimestamp))
                            .toList();
                    aggregatedTemperatures.put(entry.getKey(), temperatures);
                });

        return aggregatedTemperatures.entrySet()
                .parallelStream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }

    @Override
    public Temperature findById(Long id) {
        Optional<Temperature> temperature = temperatureRepository.findById(id);

        return temperature.orElseThrow(() -> new ObjectNotFoundException(String.format("Temperature with id %d not found.", id)));
    }
}
