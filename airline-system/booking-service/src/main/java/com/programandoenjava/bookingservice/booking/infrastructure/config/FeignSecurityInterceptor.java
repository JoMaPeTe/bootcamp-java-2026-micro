package com.programandoenjava.bookingservice.booking.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeignSecurityInterceptor implements RequestInterceptor {

    // Recuperamos la llave secreta del application.yaml del booking
    @Value("${gateway.internal.token}")
    private String gatewayInternalToken;

    @Override
    public void apply(RequestTemplate template) {
        // Antes de que Feign dispare la petición, le inyectamos la llave
        template.header("X-Gateway-Token", gatewayInternalToken);
    }
}