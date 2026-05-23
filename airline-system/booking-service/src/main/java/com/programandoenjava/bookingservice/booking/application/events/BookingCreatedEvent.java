package com.programandoenjava.bookingservice.booking.application.events;

public record BookingCreatedEvent(String bookingId, String passengerEmail) {}