package com.mpusinhol.temperaturesensorapi.resource.exception;

import com.mpusinhol.temperaturesensorapi.dto.StandardError;
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
}
