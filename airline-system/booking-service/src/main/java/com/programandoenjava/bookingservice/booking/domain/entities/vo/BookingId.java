package com.programandoenjava.bookingservice.booking.domain.entities.vo;

import java.util.UUID;

public record BookingId(UUID value) {
    public BookingId {
        if (value == null) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo");
        }
    }

    public static BookingId generate() {
        return new BookingId(UUID.randomUUID());
    }

    public static BookingId fromString(String value) {
        return new BookingId(UUID.fromString(value));
    }
}