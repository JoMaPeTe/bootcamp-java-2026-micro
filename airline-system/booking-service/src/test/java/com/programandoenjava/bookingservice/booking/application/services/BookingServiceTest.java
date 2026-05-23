package com.programandoenjava.bookingservice.booking.application.services;

import com.programandoenjava.bookingservice.booking.application.dto.BookingRequestDto;
import com.programandoenjava.bookingservice.booking.domain.entities.Booking;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingId;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingStatus;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.FlightNumber;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.PassengerId;
import com.programandoenjava.bookingservice.booking.domain.port.out.BookingRepositoryPort;
import com.programandoenjava.bookingservice.booking.domain.port.out.FlightServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private BookingRepositoryPort bookingRepository;

    @Mock
    private FlightServicePort flightServicePort; // Usamos el puerto, no el cliente Feign directamente

    @InjectMocks
    private BookingService bookingService;
    @BeforeEach
    void setUp() {
        // Así te aseguras de que el servicio SIEMPRE tiene el publisher inyectado
        bookingService.setApplicationEventPublisher(eventPublisher);
    }
    @Test
    @DisplayName("Should create a booking with PENDING status")
    void createBooking_ShouldStartAsPending() {
        // 1 Arrange
        String flightNumber = "IB123";
        Long passengerId = 45L;
        // Creamos el DTO porque ahora el servicio recibe DTO, no Strings
        BookingRequestDto request = new BookingRequestDto( flightNumber,passengerId,"test@test.com", 1);
        Booking mockBooking = new Booking(new BookingId(UUID.randomUUID()),
                new FlightNumber(flightNumber),
                new PassengerId(passengerId),
                BookingStatus.PENDING,
                request.seats(),
                150L,"mock@email.com");

        // Simulamos que hay asientos (US-004)
        given(flightServicePort.hasAvailableSeats(any(), any(Integer.class))).willReturn(true);
        given(bookingRepository.save(any(Booking.class))).willReturn(mockBooking);

        // 2 Act
        Booking result = bookingService.createBooking(request);

        // 3 Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(BookingStatus.PENDING);
        assertThat(result.getFlightNumber().value()).isEqualTo(flightNumber);
    }
}