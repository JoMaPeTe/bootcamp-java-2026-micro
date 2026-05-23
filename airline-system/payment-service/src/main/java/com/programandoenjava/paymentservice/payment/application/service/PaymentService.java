package com.programandoenjava.paymentservice.payment.application.service;

import com.programandoenjava.paymentservice.payment.application.port.in.ProcessPaymentUseCase;
import org.springframework.stereotype.Service;

@Service
public class PaymentService implements ProcessPaymentUseCase {

    @Override
    public boolean process(String email, Long amount) {
        // La regla de negocio del MVP: si el email tiene la palabra "error", falla.
        if(email==null){
            return false;
        }
        return !email.contains("error");
    }
}