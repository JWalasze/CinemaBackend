package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.models.ReservationDTO;
import com.rest.cinemaapi.models.Ticket;
import com.rest.cinemaapi.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/create-reservation")
    public ResponseEntity<Ticket> makeReservation(@RequestBody ReservationDTO reservation) {
        try {
            var ticket = this.reservationService.makeReservation(reservation);
            System.out.println(ticket.isPresent());
            return ticket.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
