package com.programandoenjava.bookingservice.booking.application.events;

public record PaymentProcessedEvent(String bookingId, String passengerEmail,Long amount,  boolean isSuccess) {}