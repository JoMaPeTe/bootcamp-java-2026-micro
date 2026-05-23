package com.programandoenjava.chekinservice.dto;

public record BoardingPassDto(
    String bookingId,
    String flightNumber,
    String seat,
    String status
) {}