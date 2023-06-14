package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.models.CinemaHallDTO;
import com.rest.cinemaapi.services.CinemaHallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/public")
public class CinemaHallController {
    private final CinemaHallService cinemaHallService;

    @Autowired
    public CinemaHallController(CinemaHallService cinemaHallService) {
        this.cinemaHallService = cinemaHallService;
    }

    @GetMapping("/all-seats-for")
    public Optional<CinemaHallDTO> getAllSeatsInHallForProgramme(@RequestParam Long id) {
        return this.cinemaHallService.getAllSeatsInHall(id);
    }
}
