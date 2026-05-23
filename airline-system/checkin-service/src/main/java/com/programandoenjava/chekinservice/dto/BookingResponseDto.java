package com.programandoenjava.chekinservice.dto;

public record BookingResponseDto(
        String status,
        String flightNumber,
        String passengerEmail
) {}