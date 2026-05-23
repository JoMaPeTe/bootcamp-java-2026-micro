package com.programandoenjava.bookingservice.booking.domain.entities.vo;

public record FlightNumber(String value) {
    public FlightNumber {
        // Regla de negocio 1 (Invariante): El número no puede estar vacío
        if (value == null || value.trim().isBlank()) {
            throw new IllegalArgumentException("El número del vuelo no puede estar vacío");
        }
        // Regla de negocio 2: Formato válido (2 letras mayúsculas + 1 a 4 números)
        if (!value.matches("^[A-Z]{2}\\d{1,4}$")) {
            throw new IllegalArgumentException("El formato del número de vuelo es inválido (ej. AV123)");
        }
    }

    public String normalizedName() {
        return value.toUpperCase();
    }

}

