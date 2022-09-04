package com.mpusinhol.temperaturesensorapi.resource;

import com.mpusinhol.temperaturesensorapi.dto.Temperature;
import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

import static com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper.toDTO;
import static com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper.toModel;

@RestController
@RequestMapping(value = "/temperatures")
@RequiredArgsConstructor
@Slf4j
public class TemperatureResource {

    private final TemperatureService temperatureService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid List<TemperatureRequest> temperatureRequest) {

        if (temperatureRequest.size() == 1) {
            var temperature = toModel(temperatureRequest.get(0));
            temperatureService.create(temperature);

            var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(temperature.getId())
                    .toUri();

            return ResponseEntity.created(uri).build();
        }

        var temperatures = temperatureRequest.stream().map(TemperatureMapper::toModel).toList();
        temperatureService.create(temperatures);

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Temperature> findById(@PathVariable Long id) {
        var temperature = temperatureService.findById(id);

        return ResponseEntity.ok(toDTO(temperature));
    }
}
