package com.programandoenjava.flightservice.flight.application.services;

import com.programandoenjava.flightservice.flight.domain.entities.Flight;
import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightNumber;
import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightPrice;
import com.programandoenjava.flightservice.flight.domain.exceptions.NotEnoughSeatsException;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsCriteria;
import com.programandoenjava.flightservice.flight.domain.port.out.FlightRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepositoryPort flightRepositoryPort;

    @InjectMocks
    private FlightService flightService;

    @Test
    @DisplayName("Should reserve seats successfully when flight exists and has enough seats")
    void reserveSeats_Success() {
        // Arrange
        String flightNumber = "IB123";
        Integer quantity = 2;
        Flight flight = createFlight(flightNumber, 10);
        given(flightRepositoryPort.findByFlightNumber(flightNumber)).willReturn(Optional.of(flight));
        given(flightRepositoryPort.guardar(any(Flight.class))).willReturn(flight);

        // Act
        Flight result = flightService.reserveSeats(flightNumber, quantity);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAvailableSeats()).isEqualTo(8);
        verify(flightRepositoryPort).findByFlightNumber(flightNumber);
        verify(flightRepositoryPort).guardar(flight);
    }

    @Test
    @DisplayName("Should throw exception when reserving seats for a non-existent flight")
    void reserveSeats_FlightNotFound() {
        // Arrange
        String flightNumber = "NONEXISTENT";
        given(flightRepositoryPort.findByFlightNumber(flightNumber)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> flightService.reserveSeats(flightNumber, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("El vuelo no existe");
        verify(flightRepositoryPort).findByFlightNumber(flightNumber);
    }

    @Test
    @DisplayName("Should throw NotEnoughSeatsException when reserving more seats than available")
    void reserveSeats_NotEnoughSeats() {
        // Arrange
        String flightNumber = "IB123";
        Integer quantity = 11;
        Flight flight = createFlight(flightNumber, 10);
        given(flightRepositoryPort.findByFlightNumber(flightNumber)).willReturn(Optional.of(flight));

        // Act & Assert
        assertThatThrownBy(() -> flightService.reserveSeats(flightNumber, quantity))
                .isInstanceOf(NotEnoughSeatsException.class)
                .hasMessage("Operación denegada: No hay suficientes asientos disponibles en este vuelo");
        verify(flightRepositoryPort).findByFlightNumber(flightNumber);
    }

    @Test
    @DisplayName("Should return list of flights when searching with criteria")
    void searchFlights_Success() {
        // Arrange
        SearchFlightsCriteria criteria = new SearchFlightsCriteria("Madrid", "Barcelona", LocalDate.now());
        List<Flight> expectedFlights = List.of(createFlight("IB123", 10));
        given(flightRepositoryPort.search(criteria)).willReturn(expectedFlights);

        // Act
        List<Flight> results = flightService.searchFlights(criteria);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFlightNumber()).isEqualTo("IB123");
        verify(flightRepositoryPort).search(criteria);
    }

    private Flight createFlight(String flightNumber, Integer availableSeats) {
        return new Flight(
                1L,
                new FlightNumber(flightNumber),
                "Madrid",
                "Barcelona",
                LocalDateTime.now().plusDays(1),
                new FlightPrice(100L),
                availableSeats
        );
    }
}
