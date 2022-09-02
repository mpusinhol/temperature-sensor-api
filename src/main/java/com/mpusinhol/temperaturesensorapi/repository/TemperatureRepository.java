package com.mpusinhol.temperaturesensorapi.repository;

import com.mpusinhol.temperaturesensorapi.model.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, Long> {
}
