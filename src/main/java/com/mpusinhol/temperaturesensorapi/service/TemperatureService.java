package com.mpusinhol.temperaturesensorapi.service;

import com.mpusinhol.temperaturesensorapi.model.Temperature;

public interface TemperatureService {

    Temperature create(Temperature temperature);

    Temperature findById(Long id);
}
