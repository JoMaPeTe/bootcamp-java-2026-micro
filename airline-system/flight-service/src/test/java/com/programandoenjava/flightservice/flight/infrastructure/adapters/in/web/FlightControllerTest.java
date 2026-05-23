
package com.programandoenjava.flightservice.flight.infrastructure.adapters.in.web;


import com.programandoenjava.flightservice.flight.domain.entities.Flight;
import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightNumber;
import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightPrice;
import com.programandoenjava.flightservice.flight.domain.port.in.ReserveSeatsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsCriteria;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = FlightController.class)
@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchFlightsUseCase searchFlightsUseCase;

    @MockitoBean
    private ReserveSeatsUseCase reserveSeatsUseCase;



    @Test
    @DisplayName("GET /api/v1/flights/search - Should return list of flights")
    void searchFlights_ShouldReturnFlights() throws Exception {
        // Arrange
        Flight flight = createFlight("IB123", 10);
        given(searchFlightsUseCase.searchFlights(any(SearchFlightsCriteria.class)))
                .willReturn(List.of(flight));

        // Act & Assert
        mockMvc.perform(get("/api/v1/flights/search")
                        .param("origin", "Madrid")
                        .param("destination", "Barcelona")
                        .param("date", "2026-05-20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].flightNumber").value("IB123"))
                .andExpect(jsonPath("$[0].origin").value("Madrid"))
                .andExpect(jsonPath("$[0].destination").value("Barcelona"))
                .andExpect(jsonPath("$[0].availableSeats").value(10));
    }

    @Test
    @DisplayName("POST /api/v1/flights/{flightNumber}/reserve - Should reserve seats")
    void reserveSeats_ShouldReturnUpdatedFlight() throws Exception {
        // Arrange
        String flightNumber = "IB123";
        Integer quantity = 2;
        Flight updatedFlight = createFlight(flightNumber, 8);
        given(reserveSeatsUseCase.reserveSeats(flightNumber, quantity))
                .willReturn(updatedFlight);

        // Act & Assert
        mockMvc.perform(post("/api/v1/flights/{flightNumber}/reserve", flightNumber)
                        .param("quantity", quantity.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flightNumber").value(flightNumber))
                .andExpect(jsonPath("$.availableSeats").value(8));
    }

    private Flight createFlight(String flightNumber, Integer availableSeats) {
        return new Flight(
                1L,
                new FlightNumber(flightNumber),
                "Madrid",
                "Barcelona",
                LocalDateTime.of(2026, 5, 17, 10, 0),
                new FlightPrice(100L),
                availableSeats
        );
    }
}