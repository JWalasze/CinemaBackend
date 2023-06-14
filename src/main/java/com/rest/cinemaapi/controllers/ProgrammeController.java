package com.rest.cinemaapi.controllers;

import com.rest.cinemaapi.enumerators.Filter;
import com.rest.cinemaapi.models.Programme;
import com.rest.cinemaapi.services.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class ProgrammeController {
    private final ProgrammeService programmeService;

    @Autowired
    public ProgrammeController(ProgrammeService programmeService) {
        this.programmeService = programmeService;
    }

    @GetMapping("/programme")
    public Page<Programme> getProgramme(
            @RequestParam Integer cinemaId,
            @RequestParam LocalDate date,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer size,
            @RequestParam(defaultValue = "NONE") Filter filterBy,
            @RequestParam(defaultValue = "") String value
    ) {
        return this.programmeService.getProgramme(cinemaId, page, size, date, filterBy, value);
    }
}
