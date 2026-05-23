package com.programandoenjava.chekinservice.listener;

import com.programandoenjava.chekinservice.event.CheckinConfirmedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component // 👈 ¡Súper importante para que Spring lo detecte!
public class CheckinNotificationListener {

    @EventListener
    public void onCheckinConfirmed(CheckinConfirmedEvent event) {
        System.out.println("========================================================");
        System.out.println("🔔 NOTIFICACIÓN: Check-in completado con éxito");
        System.out.println("📧 Enviando email a: " + event.passengerEmail());
        System.out.println("MENSAJE: Tu tarjeta de embarque para el vuelo " + event.flightNumber() + " ha sido generada.");
        System.out.println("Asiento asignado: " + event.seat());
        System.out.println("¡Buen viaje!");
        System.out.println("========================================================\n");
    }
}