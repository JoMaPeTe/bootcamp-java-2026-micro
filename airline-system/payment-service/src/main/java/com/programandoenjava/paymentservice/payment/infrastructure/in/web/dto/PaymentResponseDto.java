package com.programandoenjava.paymentservice.payment.infrastructure.in.web.dto;

public record PaymentResponseDto(String processorName, Long amount, String userEmail, Boolean paymentSucceeded,String bookingId) {}