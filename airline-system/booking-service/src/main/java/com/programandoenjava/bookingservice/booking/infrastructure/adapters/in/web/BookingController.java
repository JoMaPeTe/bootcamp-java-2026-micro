package com.programandoenjava.bookingservice.booking.infrastructure.adapters.in.web;

import com.programandoenjava.bookingservice.booking.application.dto.BookingRequestDto;
import com.programandoenjava.bookingservice.booking.application.dto.BookingResponseDto;
import com.programandoenjava.bookingservice.booking.application.services.BookingService;
import com.programandoenjava.bookingservice.booking.domain.entities.Booking;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.in.web.dto.PaymentRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto request) {
        // Ejecutamos la lógica de negocio a través del servicio
        Booking booking = bookingService.createBooking(request);
        
        // Mapeamos a un DTO de respuesta
        BookingResponseDto response = new BookingResponseDto(
                booking.getId().value().toString(),      // Extraemos el UUID y lo hacemos String
                booking.getFlightNumber().value(),       // Extraemos el String del vuelo
                booking.getPassengerId().value(),         // Extraemos el Long del pasajero
                booking.getStatus().name(),
                booking.getTotalPrice(),
                request.passengerEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/{id}/pay")
    public ResponseEntity<BookingResponseDto> payBooking(
            @PathVariable String id,
            @RequestBody PaymentRequestDto paymentRequest) {

        // Llamamos al orquestador que hemos construido
        BookingResponseDto response = bookingService.payBooking(
                id,
                paymentRequest
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable String id) {
        // 1. Busca la reserva (usa tu lógica de negocio)
      Booking booking = bookingService.getBookingById(id);

        // 2. Crea el DTO de respuesta (Copia el mapeo que ya tienes en el createBooking)
        BookingResponseDto response = new BookingResponseDto(
                id,
                booking.getFlightNumber().value(), // O el dato real del dominio
                booking.getPassengerId().value(),         // O el dato real
                booking.getStatus().name(),
                booking.getTotalPrice(),
                booking.getPassengerEmail()
        );

        return ResponseEntity.ok(response);
    }
}

