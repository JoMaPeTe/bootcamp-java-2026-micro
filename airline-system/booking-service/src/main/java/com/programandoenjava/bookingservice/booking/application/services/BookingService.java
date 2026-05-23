package com.programandoenjava.bookingservice.booking.application.services;

import com.programandoenjava.bookingservice.booking.application.dto.BookingRequestDto;
import com.programandoenjava.bookingservice.booking.application.dto.BookingResponseDto;
import com.programandoenjava.bookingservice.booking.application.events.BookingCreatedEvent;
import com.programandoenjava.bookingservice.booking.application.events.PaymentProcessedEvent;
import com.programandoenjava.bookingservice.booking.domain.entities.Booking;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingId;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingStatus;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.FlightNumber;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.PassengerId;
import com.programandoenjava.bookingservice.booking.domain.exceptions.OverbookingException;
import com.programandoenjava.bookingservice.booking.domain.port.in.CreateBookingUseCase;
import com.programandoenjava.bookingservice.booking.domain.port.out.BookingRepositoryPort;
import com.programandoenjava.bookingservice.booking.domain.port.out.FlightServicePort;
import java.util.UUID;
import com.programandoenjava.bookingservice.booking.domain.port.out.PaymentServicePort;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.in.web.dto.PaymentRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

public class BookingService implements CreateBookingUseCase, ApplicationEventPublisherAware {
    private final BookingRepositoryPort bookingRepositoryPort;
    private final FlightServicePort flightServicePort; // Puerto para hablar con Flight-Service
    private final PaymentServicePort paymentServicePort;
    private  ApplicationEventPublisher eventPublisher;
    public BookingService(BookingRepositoryPort bookingRepository, FlightServicePort flightServicePort, PaymentServicePort paymentServicePort) {
        this.bookingRepositoryPort = bookingRepository;
        this.flightServicePort = flightServicePort;
        this.paymentServicePort = paymentServicePort;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
    @Transactional
    public Booking createBooking(BookingRequestDto request) {
        // Lógica US-004: Validar disponibilidad a través del puerto
        if (!flightServicePort.hasAvailableSeats(new FlightNumber(request.flightNumber()), request.seats())) {
            throw new OverbookingException("Error");
        }
        Long pricePerSeat= flightServicePort.getFlightPrice(new FlightNumber(request.flightNumber()));
        Long totalPrice= pricePerSeat*request.seats();
        // Crear la reserva en estado PENDING
        Booking booking = new Booking(new BookingId(UUID.randomUUID()),new FlightNumber( request.flightNumber()), new PassengerId(request.passengerId()), BookingStatus.PENDING, request.seats(), totalPrice, request.passengerEmail());

        // Confirmar reserva de asientos en el otro microservicio
        flightServicePort.reserveSeats(new FlightNumber(request.flightNumber()), request.seats());
        eventPublisher.publishEvent(new BookingCreatedEvent(
                booking.getId().value().toString(),
                request.passengerEmail() // Asumiendo que lo tienes aquí
        ));
        return bookingRepositoryPort.save(booking);
    }
    @Transactional
    public BookingResponseDto payBooking(String bookingIdString, PaymentRequestDto paymentRequest) {
        // Convertimos el String a UUID (o el tipo de ID que uses) y buscamos la reserva

        UUID bookingId = UUID.fromString(bookingIdString);
        Booking booking = bookingRepositoryPort.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        try {
            // 1. Intentamos cobrar llamando al payment-service (US-005)
            boolean isSuccess = paymentServicePort.processPayment( booking.getId().value().toString(), paymentRequest.userEmail(), booking.getTotalPrice());

            // 2. Si el pago devuelve true, confirmamos
            if (isSuccess) {
                booking.confirm();
                bookingRepositoryPort.save(booking);
                eventPublisher.publishEvent(new PaymentProcessedEvent(
                        bookingIdString,
                        paymentRequest.userEmail(),
                        booking.getTotalPrice(),
                        isSuccess
                ));
                return new BookingResponseDto(booking.getId().value().toString(),
                        booking.getFlightNumber().value(),
                        booking.getPassengerId().value(),
                        booking.getStatus().name(),
                        booking.getTotalPrice(),
                        paymentRequest.userEmail()

                );
            } else {
                // Forzamos el fallo para ir al catch
                throw new RuntimeException("El proveedor rechazó el pago");
            }

        } catch (Exception e) {
            // 3. ¡COMPENSACIÓN! (US-006)

            // A. Cancelamos la reserva localmente
            booking.cancel();
            bookingRepositoryPort.save(booking);

            // B. Llamamos al flight-service para liberar las plazas
            flightServicePort.cancelReserve(booking.getFlightNumber(), booking.getSeats());

            // C. Avisamos al usuario del problema
            throw new RuntimeException("Pago fallido. Reserva cancelada y plazas liberadas. Detalle: " + e.getMessage());
        }
    }

    public Booking getBookingById(String id) {
        UUID bookingId = UUID.fromString(id);
        return bookingRepositoryPort.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
    }
}