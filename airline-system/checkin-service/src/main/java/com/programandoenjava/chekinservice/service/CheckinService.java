package com.programandoenjava.chekinservice.service;

import com.programandoenjava.chekinservice.client.BookingClient;
import com.programandoenjava.chekinservice.dto.BoardingPassDto;
import com.programandoenjava.chekinservice.dto.BookingResponseDto;
import com.programandoenjava.chekinservice.event.CheckinConfirmedEvent;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.context.ApplicationEventPublisher;

@Service
public class CheckinService implements ApplicationEventPublisherAware {

    private final BookingClient bookingClient;
    private  ApplicationEventPublisher eventPublisher;
    public CheckinService(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
    public BoardingPassDto doCheckIn(String bookingId) {
        // 1. Buscamos la reserva en el otro microservicio
        BookingResponseDto booking = bookingClient.getBooking(bookingId);

        // B. Validamos US-008
        if (!"CONFIRMED".equals(booking.status())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reserva no válida. Estado: " + booking.status());
        }

        String assignedSeat = "14A";

        // C. El salvavidas del Email: Si viene nulo desde Booking, inventamos uno para la demo
        String targetEmail = (booking.passengerEmail() != null) ? booking.passengerEmail() : "pasajero-demo@campus.com";

        // D. Lanzamos el evento US-010 usando el publisher inyectado por Aware
        eventPublisher.publishEvent(new CheckinConfirmedEvent(
                targetEmail,
                bookingId,
                booking.flightNumber(),
                assignedSeat
        ));

        // E. Devolvemos la tarjeta US-007
        return new BoardingPassDto(bookingId, booking.flightNumber(), assignedSeat, "CHECKED_IN");
    }
}