package com.programandoenjava.bookingservice.booking.domain.entities.vo;

public record PassengerId(Long value) {
    public PassengerId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("El ID del pasajero debe ser un número positivo");
        }
    }
}