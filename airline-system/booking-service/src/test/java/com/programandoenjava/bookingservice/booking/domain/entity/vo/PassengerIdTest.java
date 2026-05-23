package com.programandoenjava.bookingservice.booking.domain.entity.vo;

import com.programandoenjava.bookingservice.booking.domain.entities.vo.PassengerId;
import org.junit.jupiter.api.Test; // JUnit 5
import static org.junit.jupiter.api.Assertions.assertThrows; // JUnit 5

class PassengerIdTest { // <--- FALTA ESTA LÍNEA

    @Test
    void shouldThrowExceptionWhenIdIsNegative() {
        // Este test cubrirá la rama de la excepción del VO
        assertThrows(IllegalArgumentException.class, () -> {
            new PassengerId(-1L);
        });
    }

    @Test
    void shouldCreatePassengerIdWhenValueIsValid() {
        // Test para el camino feliz
        PassengerId id = new PassengerId(100L);
        assert id.value().equals(100L);
    }
}