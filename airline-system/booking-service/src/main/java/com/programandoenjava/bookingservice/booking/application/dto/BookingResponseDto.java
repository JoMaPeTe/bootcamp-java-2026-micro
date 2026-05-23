package com.programandoenjava.bookingservice.booking.application.dto;

import com.programandoenjava.bookingservice.booking.domain.entities.vo.FlightNumber;

public record BookingResponseDto(String id, String flightNumber, Long passengerId, String status, Long totalPrice,String passengerEmail) {
}
