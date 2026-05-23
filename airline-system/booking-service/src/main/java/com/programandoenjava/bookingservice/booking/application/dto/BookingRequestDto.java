package com.programandoenjava.bookingservice.booking.application.dto;



// DTOs necesarios
public record BookingRequestDto(String flightNumber, Long passengerId, String passengerEmail,Integer seats) {
}
