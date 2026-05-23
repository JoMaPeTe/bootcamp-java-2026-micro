package com.programandoenjava.chekinservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignSecurityInterceptor implements RequestInterceptor {

    @Value("${gateway.internal.token}")
    private String gatewayInternalToken;

    @Override
    public void apply(RequestTemplate template) {
        // 1. Inyectamos la "llave del sistema" (Para evitar el 403)
        // Esta es la que acabas de sacar del Booking Service
        template.header("X-Gateway-Token", gatewayInternalToken);
        System.out.println("Enviando Token: " + gatewayInternalToken);
        // 2. Inyectamos el JWT del usuario (Para evitar el 401)
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }
        }
    }
}