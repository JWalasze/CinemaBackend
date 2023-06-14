package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.models.FilmCharacteristics;
import com.rest.cinemaapi.services.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/characteristics")
    public FilmCharacteristics getCharacteristics() {
        return this.filmService.getAllFilmCharacteristics();
    }
}
