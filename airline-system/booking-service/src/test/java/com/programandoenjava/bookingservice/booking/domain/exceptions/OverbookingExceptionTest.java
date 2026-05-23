package com.programandoenjava.bookingservice.booking.domain.exceptions;



import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class OverbookingExceptionTest {
    @Test
    void shouldMaintainMessage() {
        String msg = "No hay suficientes asientos disponibles para este vuelo.";
        OverbookingException ex = new OverbookingException(msg);
        assertThat(ex.getMessage()).isEqualTo(msg);
    }
}