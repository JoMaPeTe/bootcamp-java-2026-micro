package com.programandoenjava.flightservice.flight.domain.entities.vo;

public record FlightPrice(Long value) {
    public FlightPrice {
        // Regla de negocio 2 (Invariante): El precio no puede ser negativo
        if (value == null || value < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
    }
}

