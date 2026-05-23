package com.programandoenjava.paymentservice.payment.infrastructure.in.web.dto;

public record PaymentRequestDto(String bookingId, String userEmail, Long amount) {}

