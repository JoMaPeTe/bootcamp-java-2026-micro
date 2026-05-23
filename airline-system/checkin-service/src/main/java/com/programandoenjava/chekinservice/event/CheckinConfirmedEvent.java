package com.programandoenjava.chekinservice.event;

public record CheckinConfirmedEvent(
        String passengerEmail, 
        String bookingId, 
        String flightNumber, 
        String seat
) {}