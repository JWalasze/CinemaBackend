package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.models.LoginFormDTO;
import com.rest.cinemaapi.models.ReservationForUsherDTO;
import com.rest.cinemaapi.models.ReservationStatusDTO;
import com.rest.cinemaapi.models.TokenJwt;
import com.rest.cinemaapi.services.ReservationService;
import com.rest.cinemaapi.services.UsherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usher")
public class UsherController {
    private final UsherService usherService;

    private final ReservationService reservationService;

    @Autowired
    public UsherController(UsherService usherService,
                           ReservationService reservationService) {
        this.usherService = usherService;
        this.reservationService = reservationService;
    }

    @PostMapping("/auth/login")
    public TokenJwt loginToCinema(@RequestBody LoginFormDTO loginFormDTO) {
        return this.usherService.loginToCinema(loginFormDTO);
    }

    @GetMapping("/check-ticket")
    public ResponseEntity<ReservationForUsherDTO> getReservation(@RequestParam Long forId) {
        var reservation = this.reservationService.getReservation(forId);
        return reservation.map(ResponseEntity::ok).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/validate-ticket")
    public ResponseEntity<ReservationStatusDTO> validateReservation(@RequestParam Long forId) {
        return this.reservationService.validateReservation(forId);
    }
}
