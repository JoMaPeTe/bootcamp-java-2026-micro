package com.programandoenjava.bookingservice.booking.infrastructure.config;

import feign.Logger;
import feign.Request;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                3000,   // connectTimeout: 3 segundos
                8000    // readTimeout: 8 segundos
        );
    }

    @Bean
    public Logger.Level logLevel() {
        return Logger.Level.FULL;  // Logging completo de Feign para debug
    }
    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "bootcamp2026");
    }
}