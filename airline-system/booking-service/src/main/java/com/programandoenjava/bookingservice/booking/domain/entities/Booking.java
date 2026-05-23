package com.programandoenjava.bookingservice.booking.domain.entities;

import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingId;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.BookingStatus;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.FlightNumber;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.PassengerId;

import java.util.UUID;
import com.programandoenjava.bookingservice.booking.domain.entities.vo.*;



public class Booking {
        private final BookingId id;
        private  FlightNumber flightNumber;
        private  PassengerId passengerId;
        private BookingStatus status;
         private Integer seats;
         private Long totalPrice;
    private String passengerEmail;
         public String getPassengerEmail() {
    return passengerEmail;
}
public void setPassengerEmail(String passengerEmail) {
    this.passengerEmail = passengerEmail;
}

        public Booking(BookingId id, FlightNumber flightNumber, PassengerId passengerId, BookingStatus status, Integer seats, Long totalPrice, String passengerEmail) {
            this.id = id;
            this.flightNumber = flightNumber;
            this.passengerId = passengerId;
            this.status = status;
            this.seats = seats;
            this.totalPrice = totalPrice;
            this.passengerEmail = passengerEmail;
        }

        // Getters...
        public BookingId getId() { return id; }
        public FlightNumber getFlightNumber() { return flightNumber; }
        public PassengerId getPassengerId() { return passengerId; }
        public BookingStatus getStatus() { return status; }
     // Aquí usaremos "PENDING" como pide la US-003

    public void setPassengerId(PassengerId passengerId) {
        this.passengerId = passengerId;
    }

    public void setFlightNumber(FlightNumber flightNumber) {
        this.flightNumber = flightNumber;
    }
    public void setSeats(Integer seats) {
        this.seats = seats;
    }
    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
    public Integer getSeats() {
        return seats;
    }
    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public Long getTotalPrice() {

        return totalPrice;
    }
}