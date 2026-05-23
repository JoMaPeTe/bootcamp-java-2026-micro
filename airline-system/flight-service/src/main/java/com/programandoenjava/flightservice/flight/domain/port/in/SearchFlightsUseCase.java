package com.programandoenjava.flightservice.flight.domain.port.in;

import com.programandoenjava.flightservice.flight.domain.entities.Flight;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsCriteria;

import java.util.List;

public interface SearchFlightsUseCase {
    // Devuelve una lista de entidades de dominio
    // SearchFlightsCriteria está bien aquí, siempre que sea una clase/record definida dentro de 'domain'
    List<Flight> searchFlights(SearchFlightsCriteria criteria);

    Flight findByFlightNumber(String flightNumber);
}