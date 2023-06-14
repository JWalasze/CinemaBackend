package com.rest.cinemaapi.models;

import com.rest.cinemaapi.enumerators.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationStatusDTO {
    private Long reservationId;

    private ReservationStatus status;

    private String message;

    public ReservationStatusDTO() {
    }

    public ReservationStatusDTO(Long reservationId, ReservationStatus status, String message) {
        this.reservationId = reservationId;
        this.status = status;
        this.message = message;
    }
}
