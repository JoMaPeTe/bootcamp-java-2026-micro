package com.programandoenjava.paymentservice.payment.application.port.in;

public interface ProcessPaymentUseCase {
    boolean process(String email, Long amount);
}