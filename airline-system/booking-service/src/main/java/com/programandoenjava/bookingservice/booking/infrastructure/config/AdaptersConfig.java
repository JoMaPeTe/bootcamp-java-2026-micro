package com.programandoenjava.bookingservice.booking.infrastructure.config;

import com.programandoenjava.bookingservice.booking.application.services.BookingService;
import com.programandoenjava.bookingservice.booking.domain.port.in.CreateBookingUseCase;
import com.programandoenjava.bookingservice.booking.domain.port.out.BookingRepositoryPort;
import com.programandoenjava.bookingservice.booking.domain.port.out.FlightServicePort;
import com.programandoenjava.bookingservice.booking.domain.port.out.PaymentServicePort;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.out.feign.FlightClient;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.out.feign.FlightServiceAdapter;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.out.persistence.BookingPersistenceAdapter;
import com.programandoenjava.bookingservice.booking.infrastructure.adapters.out.persistence.repository.BookingJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AdaptersConfig {

    // 1. Adaptador de Salida: Persistencia (Base de Datos)
    @Bean
    public BookingRepositoryPort BookingRepositoryPort(BookingJpaRepository jpaRepository) {
        return new BookingPersistenceAdapter(jpaRepository);
    }

    // 2. Adaptador de Salida: Comunicación con Flight Service (Feign)
    @Bean
    public FlightServicePort FlightServicePort(FlightClient flightClient) {
        return new FlightServiceAdapter(flightClient);
    }

    // 3. El Servicio de Aplicación (Domain Service)
    // Aquí es donde inyectas los dos puertos definidos arriba
    @Bean
    public BookingService BookingService(BookingRepositoryPort repo, FlightServicePort flightPort, PaymentServicePort paymentPort) {
        return new BookingService(repo, flightPort, paymentPort);
    }

    // 4. Exponer los Casos de Uso (Puertos de Entrada)
    // Esto es lo que usará tu BookingController
    @Bean
    public CreateBookingUseCase CreateBookingUseCase(BookingService bookingService) {
        return bookingService;
    }
}