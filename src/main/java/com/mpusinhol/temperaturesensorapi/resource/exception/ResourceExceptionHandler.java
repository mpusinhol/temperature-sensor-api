package com.mpusinhol.temperaturesensorapi.resource.exception;

import com.mpusinhol.temperaturesensorapi.dto.StandardError;
import com.mpusinhol.temperaturesensorapi.exception.DuplicatedTemperatureException;
import com.mpusinhol.temperaturesensorapi.exception.InvalidAggregationModeException;
import com.mpusinhol.temperaturesensorapi.exception.InvalidTemperatureUnitException;
import com.mpusinhol.temperaturesensorapi.exception.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException exception, HttpServletRequest request) {
        StandardError error = new StandardError(HttpStatus.NOT_FOUND.value(), exception.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidTemperatureUnitException.class)
    public ResponseEntity<StandardError> invalidTemperatureUnit(InvalidTemperatureUnitException exception, HttpServletRequest request) {
        StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicatedTemperatureException.class)
    public ResponseEntity<StandardError> constraintViolation(DuplicatedTemperatureException exception, HttpServletRequest request) {
        StandardError error = new StandardError(HttpStatus.CONFLICT.value(), exception.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(InvalidAggregationModeException.class)
    public ResponseEntity<StandardError> invalidAggregationMode(InvalidAggregationModeException exception, HttpServletRequest request) {
        StandardError error = new StandardError(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), Instant.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
