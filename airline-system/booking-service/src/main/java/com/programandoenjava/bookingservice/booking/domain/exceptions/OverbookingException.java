package com.programandoenjava.bookingservice.booking.domain.exceptions;

public class OverbookingException extends RuntimeException {
    public OverbookingException(String error) {
        super("No hay suficientes asientos disponibles para este vuelo.");
    }
}