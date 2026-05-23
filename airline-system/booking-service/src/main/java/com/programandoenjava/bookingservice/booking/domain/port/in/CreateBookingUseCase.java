package com.programandoenjava.bookingservice.booking.domain.port.in;

import com.programandoenjava.bookingservice.booking.application.dto.BookingRequestDto;
import com.programandoenjava.bookingservice.booking.domain.entities.Booking;

public interface CreateBookingUseCase {
    Booking createBooking(BookingRequestDto request);
}
