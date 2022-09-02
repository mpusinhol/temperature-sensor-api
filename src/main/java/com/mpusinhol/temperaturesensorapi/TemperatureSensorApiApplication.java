package com.mpusinhol.temperaturesensorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TemperatureSensorApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemperatureSensorApiApplication.class, args);
    }

}
