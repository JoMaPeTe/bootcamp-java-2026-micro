package com.programandoenjava.flightservice.flight.domain.port.in;



import java.time.LocalDate;

/**
 * Objeto que encapsula los criterios de búsqueda de vuelos.
 * Pertenece al dominio porque define qué cosas nos importan para buscar.
 */
public record SearchFlightsCriteria(
        String origin,
        String destination,
        LocalDate departureDate // Usamos LocalDate porque solemos buscar por día, no por la hora exacta
) {
    public SearchFlightsCriteria {
        if (origin == null || destination == null) {
            throw new IllegalArgumentException("El origen y el destino son obligatorios para buscar");
        }
    }
}