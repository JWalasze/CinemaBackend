package com.rest.cinemaapi.models;

import com.rest.cinemaapi.enumerators.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationForUsherDTO {
    private Long id;

    private ContactData contactData;

    private ReservationStatus reservationStatus;

    private String film;

    private LocalDateTime dateTime;

    private String hallName;

    private Integer numberOfSeats;

    public ReservationForUsherDTO() {
    }

    public ReservationForUsherDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.contactData = reservation.getContactData();
        this.reservationStatus = reservation.getReservationStatus();
        this.film = reservation.getProgrammeFilm().getFilm().getName();
        this.dateTime = reservation.getProgrammeFilm().getDate();
        this.hallName = reservation.getProgrammeFilm().getHall().getHallName();
        this.numberOfSeats = reservation.getReservedSeats().size();
    }
}
