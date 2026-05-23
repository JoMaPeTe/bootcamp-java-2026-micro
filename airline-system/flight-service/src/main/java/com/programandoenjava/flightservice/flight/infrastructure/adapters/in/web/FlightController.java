package com.programandoenjava.flightservice.flight.infrastructure.adapters.in.web;

import com.programandoenjava.flightservice.flight.application.dto.FlightDto;
import com.programandoenjava.flightservice.flight.domain.entities.Flight;
import com.programandoenjava.flightservice.flight.domain.port.in.ReserveSeatsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsCriteria;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final SearchFlightsUseCase searchFlightsUseCase;
    private final ReserveSeatsUseCase reserveSeatsUseCase;
    // Inyectamos la interfaz del caso de uso, ¡no la implementación concreta! (Desacoplamiento total)
    public FlightController(SearchFlightsUseCase searchFlightsUseCase, ReserveSeatsUseCase reserveSeatsUseCase) {
        this.searchFlightsUseCase = searchFlightsUseCase;
        this.reserveSeatsUseCase = reserveSeatsUseCase;
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // 1. Preparamos el comando para el dominio
        SearchFlightsCriteria criteria = new SearchFlightsCriteria(origin, destination, date);

        // 2. Ejecutamos el caso de uso
        List<Flight> flights = searchFlightsUseCase.searchFlights(criteria);

        // 3. Traducimos las entidades de dominio a DTOs para la respuesta HTTP
        List<FlightDto> response = flights.stream()
                .map(FlightDto::fromDomain)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    // 3. Nuestro nuevo endpoint de reserva
    @PostMapping("/{flightNumber}/reserve")
    public ResponseEntity<FlightDto> reserveSeats(
            @PathVariable String  flightNumber,
            @RequestParam Integer quantity) {

        // Ejecutamos el caso de uso (nuestro dominio validará si hay asientos y hará el Lock Pesimista)
        Flight updatedFlight = reserveSeatsUseCase.reserveSeats(flightNumber, quantity);

        // Traducimos la entidad actualizada a DTO y la devolvemos
        return ResponseEntity.ok(FlightDto.fromDomain(updatedFlight));
    }
    @GetMapping("/{flightNumber}")
    public ResponseEntity<FlightDto> getFlightByNumber(@PathVariable String flightNumber) {

        Flight flight = searchFlightsUseCase.findByFlightNumber(flightNumber);
        if (flight == null) {
            return ResponseEntity.notFound().build();
        }
        // 2. Si el dominio devuelve el vuelo, lo pasamos a DTO
        return ResponseEntity.ok(FlightDto.fromDomain(flight));
    }
    @PostMapping("/{flightNumber}/cancel-reserve")
    public ResponseEntity<Void> cancelReserve(
            @PathVariable String flightNumber,
            @RequestParam Integer quantity) {

        // Este caso de uso hará el "quantity * -1" o sumará los asientos
        reserveSeatsUseCase.cancelReservation(flightNumber, quantity);
        return ResponseEntity.noContent().build();
    }
}