package com.mpusinhol.temperaturesensorapi.resource;

import com.mpusinhol.temperaturesensorapi.dto.Temperature;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper.toDTO;
import static com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper.toModel;

@RestController
@RequestMapping(value = "/temperatures")
@RequiredArgsConstructor
public class TemperatureResource {

    private final TemperatureService temperatureService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Temperature temperatureRequest) {
        var temperature = toModel(temperatureRequest);
        temperature = temperatureService.create(temperature);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(temperature.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Temperature> findById(@PathVariable Long id) {
        var temperature = temperatureService.findById(id);

        return ResponseEntity.ok(toDTO(temperature));
    }
}
