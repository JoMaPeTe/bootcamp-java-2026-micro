package com.programandoenjava.flightservice.flight.domain.port.in;

import com.programandoenjava.flightservice.flight.domain.entities.Flight;

public interface ReserveSeatsUseCase {
    // Devuelve la entidad de dominio, no un DTO
    Flight reserveSeats(String flightNumber, Integer quantity);

    void cancelReservation(String flightNumber, Integer quantity);
}
