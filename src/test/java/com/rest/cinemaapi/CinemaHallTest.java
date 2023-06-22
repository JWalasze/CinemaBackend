package com.rest.cinemaapi;

import com.rest.cinemaapi.enumerators.*;
import com.rest.cinemaapi.models.*;
import com.rest.cinemaapi.repositories.ProgrammeRepository;
import com.rest.cinemaapi.repositories.ReservedSeatRepository;
import com.rest.cinemaapi.services.CinemaHallService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CinemaHallTest {
    @Mock
    ReservedSeatRepository reservedSeatRepository;

    @Mock
    ProgrammeRepository programmeRepository;

    @InjectMocks
    CinemaHallService cinemaHallService;

    private Film film;

    private CinemaHall cinemaHall;

    private List<Seat> seats;

    private List<ReservedSeat> reservedSeats;

    public void setup() {
        this.film = new Film(
                "Star Wars",
                "Gwiezdne przygody",
                "Reżyser",
                "Wiele osób",
                BigDecimal.valueOf(25.5), LocalDate.of(1975, 12, 10),
                LocalTime.of(10,30),
                AgeLimit.PG18,
                Genre.ANIMATED,
                "https://domena.1234",
                FilmLanguageType.DUBBING,
                FilmScreenType.SCREEN_2D,
                FilmType.DOLBY_ATMOS);

        this.cinemaHall = new CinemaHall(
                "3B",
                HallType.SCREEN_4DX,
                new Cinema("Magnolia Park", new Address("Wrocław", "500-45", "Legnicka", "23"))
        );

        this.seats = List.of(
                new Seat(10, 11, 10, 11, this.cinemaHall, SeatSection.B),
                new Seat(2, 3, 2, 3, this.cinemaHall, SeatSection.A)
        );

        this.cinemaHall.getSeats().addAll(this.seats);

        this.reservedSeats = List.of(
                new ReservedSeat(
                        new Reservation(null, null),
                        this.seats.get(0),
                        null));
    }

    @Test
    public void testCinemaHallService() {
        this.setup();
        var programmeId = 1L;

        when(programmeRepository.findById(programmeId)).thenReturn(Optional.of(new Programme(
                LocalDateTime.of(LocalDate.of(2023, 12, 25), LocalTime.of(10, 30, 0, 0)),
                film,
                cinemaHall
        )));

        when(reservedSeatRepository.findAllByProgrammeId(programmeId)).thenReturn(
                this.reservedSeats
        );

        var cinemaHall = this.cinemaHallService.getAllSeatsInHall(programmeId);
        Assert.assertFalse(cinemaHall.isEmpty());
    }

    @Test
    public void testNumberOfHallSeats() {
        this.setup();
        var programmeId = 1L;

        when(programmeRepository.findById(programmeId)).thenReturn(Optional.of(new Programme(
                LocalDateTime.of(LocalDate.of(2023, 12, 25), LocalTime.of(10, 30, 0, 0)),
                film,
                cinemaHall
        )));

        when(reservedSeatRepository.findAllByProgrammeId(programmeId)).thenReturn(
                this.reservedSeats
        );

        var cinemaHall = this.cinemaHallService.getAllSeatsInHall(programmeId);
        Assert.assertFalse(cinemaHall.isEmpty());

        var cinemaHall1 = cinemaHall.get();
        var numberOfSeats = cinemaHall1.getSeats().size();
        Assert.assertEquals(2L, numberOfSeats);
    }

    @Test
    public void testNumberOfReservedSeats() {
        this.setup();
        var programmeId = 1L;

        when(programmeRepository.findById(programmeId)).thenReturn(Optional.of(new Programme(
                LocalDateTime.of(LocalDate.of(2023, 12, 25), LocalTime.of(10, 30, 0, 0)),
                film,
                cinemaHall
        )));

        when(reservedSeatRepository.findAllByProgrammeId(programmeId)).thenReturn(
                this.reservedSeats
        );

        var cinemaHall = this.cinemaHallService.getAllSeatsInHall(programmeId);
        Assert.assertFalse(cinemaHall.isEmpty());

        var cinemaHall1 = cinemaHall.get();
        var numberOfReservedSeats = 0;
        for (SpecifiedSeat seat : cinemaHall1.getSeats()) {
            if (seat.getIsOccupied()) {
                numberOfReservedSeats += 1;
            }
        }
        Assert.assertEquals(1L, numberOfReservedSeats);
    }
}
