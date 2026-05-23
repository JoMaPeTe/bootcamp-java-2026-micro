package com.programandoenjava.bookingservice.booking.infrastructure.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programandoenjava.bookingservice.booking.application.dto.BookingRequestDto;
import com.programandoenjava.bookingservice.booking.application.services.BookingService;
import com.programandoenjava.bookingservice.booking.domain.entities.Booking;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingId;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingStatus;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.FlightNumber;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.PassengerId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean  // Reemplaza la configuración manual de Copilot por esto
    private BookingService bookingService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Saltamos la seguridad
    @DisplayName("US-003: Debería crear una reserva exitosamente (201)")
    void createBooking_Success() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        Booking booking = new Booking(new BookingId(id),                    // Envolvemos el UUID 'id'
                new FlightNumber("IB123"),            // Envolvemos el String
                new PassengerId(1L),                  // Envolvemos el Long
                BookingStatus.CONFIRMED,
                2,
                150L,
                "mock@email.com");
        given(bookingService.createBooking(any())).willReturn(booking);

        BookingRequestDto request = new BookingRequestDto(  "IB123", 1L, "test@test.com",2);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bookings")
                        .with(csrf()) // Necesario si no desactivaste CSRF en SecurityConfig
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("US-003: Debería fallar si el servicio lanza una excepción (500/400)")
    void createBooking_InternalServerError() throws Exception {
        // Arrange
        given(bookingService.createBooking(any()))
                .willThrow(new RuntimeException("No hay asientos disponibles"));

        BookingRequestDto request = new BookingRequestDto("IB123", 1L, "test@test.com",2);

        // Act & Assert
        mockMvc.perform(post("/api/v1/bookings")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError()); // O el código que maneje tu GlobalExceptionHandler
    }
}