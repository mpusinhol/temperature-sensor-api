package com.mpusinhol.temperaturesensorapi.exception;

public class InvalidTemperatureUnitException extends RuntimeException {

    public InvalidTemperatureUnitException(String message) {
        super(message);
    }

    public InvalidTemperatureUnitException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
