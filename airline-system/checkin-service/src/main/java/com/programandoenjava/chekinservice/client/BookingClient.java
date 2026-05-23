package com.programandoenjava.chekinservice.client;

import com.programandoenjava.chekinservice.config.FeignSecurityInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.programandoenjava.chekinservice.dto.BookingResponseDto;
// 👇 Fíjate en la propiedad "url". Pon ahí el puerto en el que arranca tu booking-service.
@FeignClient(name = "booking-client", url = "http://localhost:8082/api/v1/bookings",configuration = FeignSecurityInterceptor.class)
public interface BookingClient {
    
    @GetMapping("/{id}")
    BookingResponseDto getBooking(@PathVariable("id") String id);
    
}