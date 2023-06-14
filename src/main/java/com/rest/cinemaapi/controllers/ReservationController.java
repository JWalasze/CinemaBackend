package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.models.ReservationDTO;
import com.rest.cinemaapi.models.Ticket;
import com.rest.cinemaapi.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create-reservation")
    public List<Ticket> makeReservation(@RequestBody ReservationDTO reservation) {
        return this.reservationService.makeReservation(reservation);
    }
}
