package com.programandoenjava.flightservice.flight.application.services;

import com.programandoenjava.flightservice.flight.domain.entities.Flight;
import com.programandoenjava.flightservice.flight.domain.port.in.ReserveSeatsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsCriteria;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.out.FlightRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class FlightService implements ReserveSeatsUseCase, SearchFlightsUseCase {

    private final FlightRepositoryPort flightRepositoryPort;

    public FlightService(FlightRepositoryPort flightRepositoryPort) {
        this.flightRepositoryPort = flightRepositoryPort;
    }

    @Override
    @Transactional
    public Flight reserveSeats(String flightNumber, Integer quantity) {
        Flight flight = flightRepositoryPort.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("El vuelo no existe"));

        flight.reserveSeats(quantity);

        return flightRepositoryPort.guardar(flight);
    }

    @Override
    @Transactional
    public void cancelReservation(String flightNumber, Integer quantity) {
        Flight flight = flightRepositoryPort.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("El vuelo no existe"));

        flight.releaseSeats(quantity);

        flightRepositoryPort.guardar(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> searchFlights(SearchFlightsCriteria criteria) {
        return flightRepositoryPort.search(criteria);
    }

    @Override
    @Transactional(readOnly = true)
    public Flight findByFlightNumber(String flightNumber) {
        return flightRepositoryPort.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new RuntimeException("El vuelo no existe"));
    }
}
