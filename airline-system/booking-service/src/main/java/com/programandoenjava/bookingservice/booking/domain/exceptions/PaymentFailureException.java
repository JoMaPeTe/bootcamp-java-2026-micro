package com.programandoenjava.bookingservice.booking.domain.exceptions;

public class PaymentFailureException extends RuntimeException {
    public PaymentFailureException(String message) {
        super(message);
    }
}
