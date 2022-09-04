package com.mpusinhol.temperaturesensorapi.exception;

public class DuplicatedTemperatureException extends RuntimeException {

    public DuplicatedTemperatureException(String message) {
        super(message);
    }

    public DuplicatedTemperatureException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
