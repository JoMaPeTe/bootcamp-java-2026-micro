package com.programandoenjava.flightservice.flight.domain.entities;

import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightNumber;
import com.programandoenjava.flightservice.flight.domain.entities.vo.FlightPrice;
import com.programandoenjava.flightservice.flight.domain.exceptions.NotEnoughSeatsException;

import java.time.LocalDateTime;

public class Flight {
    private Long id;
    private FlightNumber flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private FlightPrice price;
    private Integer availableSeats;

    public Flight(Long id, FlightNumber flightNumber, String origin, String destination,
                  LocalDateTime departureTime, FlightPrice price, Integer availableSeats) {

        // Invariante 1: Los asientos disponibles no pueden ser negativos
        if (availableSeats == null || availableSeats < 0) {
            throw new IllegalArgumentException("Los asientos disponibles no pueden ser menores a 0");
        }

        // Invariante 2: La fecha de salida debe existir (puedes añadir validación de fecha futura si lo deseas)
        if (departureTime == null) {
            throw new IllegalArgumentException("La fecha de salida es obligatoria");
        }

        // Invariante 3: Origen y destino no pueden estar vacíos ni ser iguales
        if (origin == null || destination == null || origin.equalsIgnoreCase(destination)) {
            throw new IllegalArgumentException("El origen y destino deben ser válidos y diferentes");
        }

        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.price = price;
        this.availableSeats = availableSeats;
    }

    // Equivalente a decreaseStock()
    public void reserveSeats(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad de asientos a reservar debe ser mayor que cero");
        }
        if (quantity > this.availableSeats) {
            throw new NotEnoughSeatsException("Operación denegada: No hay suficientes asientos disponibles en este vuelo");
        }
        this.availableSeats -= quantity;
    }

    // Equivalente a increaseStock() - Útil para el patrón Saga si un pago falla
    public void releaseSeats(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad de asientos a liberar debe ser mayor que cero");
        }
        this.availableSeats += quantity;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFlightNumber() {
        return flightNumber.value(); // Asumiendo que tu VO tiene un método value()
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public Long getPrice() {
        return price.value(); // Asumiendo que tu VO maneja el valor (idealmente BigDecimal)
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }


}