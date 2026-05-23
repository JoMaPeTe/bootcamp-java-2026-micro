package com.programandoenjava.flightservice.flight.application.dto;

import com.programandoenjava.flightservice.flight.domain.entities.Flight;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para representar la información de un vuelo en las búsquedas.
 * * @param id Identificador único del vuelo.
 * @param flightNumber Código de vuelo (ej: AV123).
 * @param origin Ciudad o aeropuerto de origen.
 * @param destination Ciudad o aeropuerto de destino.
 * @param departureTime Fecha y hora de salida (Requerido para US-002).
 * @param price Precio del asiento.
 * @param availableSeats Asientos disponibles (sustituye a 'stock').
 */
public record FlightDto(
        Long id,
        String flightNumber,
        String origin,
        String destination,
        LocalDateTime departureTime,
        BigDecimal price,
        Integer availableSeats
) {
    // Método estático para transformar un Flight del dominio a un DTO de salida
    public static FlightDto fromDomain(Flight flight) {
        return new FlightDto(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getOrigin(),
                flight.getDestination(),
                flight.getDepartureTime(),
                BigDecimal.valueOf(flight.getPrice()), // Aseguramos la conversión a BigDecimal
                flight.getAvailableSeats()
        );
    }
}