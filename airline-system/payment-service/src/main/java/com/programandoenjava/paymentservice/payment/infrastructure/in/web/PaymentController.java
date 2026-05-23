package com.programandoenjava.paymentservice.payment.infrastructure.in.web;

import com.programandoenjava.paymentservice.payment.application.port.in.ProcessPaymentUseCase;
import com.programandoenjava.paymentservice.payment.infrastructure.in.web.dto.PaymentRequestDto;
import com.programandoenjava.paymentservice.payment.infrastructure.in.web.dto.PaymentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final ProcessPaymentUseCase processPaymentUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
    }
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto request) {

        // Lógica del tu MockProcessor: Si el email tiene "error", falla. Si no, éxito.
        boolean isSuccess =processPaymentUseCase.process(request.userEmail(), request.amount());

        PaymentResponseDto response = new PaymentResponseDto(
                "MockProcessor MVP",
                request.amount(),
                request.userEmail(),
                isSuccess,
                request.bookingId()
        );

        return ResponseEntity.ok(response);
    }
}