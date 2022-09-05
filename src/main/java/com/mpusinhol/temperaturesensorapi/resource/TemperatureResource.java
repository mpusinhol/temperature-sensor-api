package com.mpusinhol.temperaturesensorapi.resource;

import com.mpusinhol.temperaturesensorapi.dto.AggregationMode;
import com.mpusinhol.temperaturesensorapi.dto.TemperatureRequest;
import com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper;
import com.mpusinhol.temperaturesensorapi.model.Temperature;
import com.mpusinhol.temperaturesensorapi.service.TemperatureService;
import com.mpusinhol.temperaturesensorapi.validation.ValidAggregationMode;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static com.mpusinhol.temperaturesensorapi.mapper.TemperatureMapper.toModel;

@RestController
@RequestMapping(value = "/temperatures")
@RequiredArgsConstructor
@Slf4j
@Validated
public class TemperatureResource {

    private final TemperatureService temperatureService;

    @PostMapping
    public ResponseEntity<Void> create(
            @RequestBody @Valid
            List<TemperatureRequest> temperatureRequest) {

        if (temperatureRequest.size() == 1) {
            Temperature temperature = toModel(temperatureRequest.get(0));
            temperatureService.create(temperature);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(temperature.getId())
                    .toUri();

            return ResponseEntity.created(uri).build();
        }

        List<Temperature> temperatures = temperatureRequest.stream().map(TemperatureMapper::toModel).toList();
        temperatureService.create(temperatures);

        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Temperature>>> findAll(
            @RequestParam(value = "aggregate", required = false)
            @Valid
            @ValidAggregationMode
            @Parameter(allowEmptyValue = true, description = "Available values are DAILY, HOURLY or NONE")
            String aggregate) {

        AggregationMode aggregationMode = aggregate == null ? AggregationMode.NONE : AggregationMode.valueOf(aggregate.toUpperCase());
        Map<String, List<Temperature>> aggregatedTemperatures = temperatureService.findAll(aggregationMode);

        return ResponseEntity.ok(aggregatedTemperatures);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Temperature> findById(@PathVariable Long id) {
        var temperature = temperatureService.findById(id);

        return ResponseEntity.ok(temperature);
    }
}
