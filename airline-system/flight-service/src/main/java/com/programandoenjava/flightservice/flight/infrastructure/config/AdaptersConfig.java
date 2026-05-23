package com.programandoenjava.flightservice.flight.infrastructure.config;

import com.programandoenjava.flightservice.flight.application.services.FlightService;
import com.programandoenjava.flightservice.flight.domain.port.in.ReserveSeatsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.in.SearchFlightsUseCase;
import com.programandoenjava.flightservice.flight.domain.port.out.FlightRepositoryPort;
import com.programandoenjava.flightservice.flight.infrastructure.adapters.out.persistence.FlightJpaAdapter;
import com.programandoenjava.flightservice.flight.infrastructure.adapters.out.persistence.repository.SpringDataFlightRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdaptersConfig {

    // 1. Configuramos el Adaptador de Salida (Base de datos)
    @Bean
    public FlightRepositoryPort flightRepositoryPort(SpringDataFlightRepository jpaRepository) {
        return new FlightJpaAdapter(jpaRepository);
    }

    // 2. Configuramos el Servicio de Aplicación inyectándole el puerto recién creado
    @Bean
    public FlightService flightService(FlightRepositoryPort flightRepositoryPort) {
        return new FlightService(flightRepositoryPort);
    }

    // 3. Exponemos los Casos de Uso
    @Bean
    public SearchFlightsUseCase searchFlightsUseCase(FlightService flightService) {
        return flightService;
    }

    @Bean
    public ReserveSeatsUseCase reserveSeatsUseCase(FlightService flightService) {
        return flightService;
    }
}