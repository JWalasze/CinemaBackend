package com.rest.cinemaapi.services;

import com.rest.cinemaapi.models.Film;
import com.rest.cinemaapi.models.FilmCharacteristics;
import com.rest.cinemaapi.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public FilmCharacteristics getAllFilmCharacteristics() {
        return new FilmCharacteristics();
    }
}
