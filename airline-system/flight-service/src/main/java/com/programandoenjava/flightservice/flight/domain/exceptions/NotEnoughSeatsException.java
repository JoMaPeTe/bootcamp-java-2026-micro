package com.programandoenjava.flightservice.flight.domain.exceptions;

public class NotEnoughSeatsException extends RuntimeException {
    public NotEnoughSeatsException(String message) {
        super(message);
    }
}

