package com.mpusinhol.temperaturesensorapi.exception;

public class InvalidAggregationModeException extends RuntimeException {

    public InvalidAggregationModeException(String message) {
        super(message);
    }

    public InvalidAggregationModeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
