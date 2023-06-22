package com.rest.cinemaapi;

import com.rest.cinemaapi.enumerators.*;
import com.rest.cinemaapi.models.*;
import com.rest.cinemaapi.repositories.ProgrammeRepository;
import com.rest.cinemaapi.repositories.ReservationRepository;
import com.rest.cinemaapi.repositories.ReservedSeatRepository;
import com.rest.cinemaapi.repositories.SeatRepository;
import com.rest.cinemaapi.services.CinemaHallService;
import com.rest.cinemaapi.services.ReservationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationTest {
    @Mock
    ReservedSeatRepository reservedSeatRepository;

    @Mock
    ProgrammeRepository programmeRepository;

    @Mock
    SeatRepository seatRepository;

    @Mock
    ReservationRepository reservationRepository;

    @InjectMocks
    ReservationService reservationService;

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
                new Seat(20, 3, 20, 3, this.cinemaHall, SeatSection.A),
                new Seat(30,3,30,3,this.cinemaHall, SeatSection.A)
        );

        this.cinemaHall.getSeats().addAll(this.seats);
    }

    @Test
    public void makeReservationTest() {
        this.setup();
        var reservationDTO = new ReservationDTO(
                new ContactData("Kuba", "Walaszek", "500900700", "kuba@gmail.com"),
                1L,
                List.of(1L,2L,3L)
        );

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(0)))
                .thenReturn(Optional.ofNullable(this.seats.get(0)));

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(1)))
                .thenReturn(Optional.ofNullable(this.seats.get(1)));

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(2)))
                .thenReturn(Optional.ofNullable(this.seats.get(2)));

        when(programmeRepository.findById(reservationDTO.getProgrammeId())).thenReturn(Optional.of(new Programme(
                LocalDateTime.of(LocalDate.of(2023, 12, 25), LocalTime.of(10, 30, 0, 0)),
                film,
                cinemaHall
        )));

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(0),
                null))
                .thenReturn(
                        Optional.empty()
                );

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(1),
                null))
                .thenReturn(
                        Optional.empty()
                );

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(2),
                null))
                .thenReturn(
                        Optional.of(new ReservedSeat(
                                null,
                                this.seats.get(2),
                                null))
                );

        var reservation = this.reservationService.makeReservation(reservationDTO);
        Assert.assertTrue(reservation.isEmpty());
    }

    @Test
    public void makeValidReservation() {
        this.setup();
        var reservationDTO = new ReservationDTO(
                new ContactData("Kuba", "Walaszek", "500900700", "kuba@gmail.com"),
                1L,
                List.of(1L,2L,3L)
        );

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(0)))
                .thenReturn(Optional.ofNullable(this.seats.get(0)));

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(1)))
                .thenReturn(Optional.ofNullable(this.seats.get(1)));

        when(seatRepository.findById(reservationDTO.getReservedSeatsIds().get(2)))
                .thenReturn(Optional.ofNullable(this.seats.get(2)));

        when(programmeRepository.findById(reservationDTO.getProgrammeId())).thenReturn(Optional.of(new Programme(
                LocalDateTime.of(LocalDate.of(2023, 12, 25), LocalTime.of(10, 30, 0, 0)),
                film,
                cinemaHall
        )));

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(0),
                null))
                .thenReturn(
                        Optional.empty()
                );

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(1),
                null))
                .thenReturn(
                        Optional.empty()
                );

        when(reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(
                reservationDTO.getReservedSeatsIds().get(2),
                null))
                .thenReturn(
                        Optional.empty()
                );

        var reservation = this.reservationService.makeReservation(reservationDTO);
        Assert.assertFalse(reservation.isEmpty());
    }

    @Test
    public void validateReservation1Test() {
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.empty());

        var response = this.reservationService.validateReservation(1L);
        Assert.assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Ticket does not exist!");
        Assert.assertEquals(response.getBody().getStatus(), ReservationStatus.NOT_EXISTS);
    }

    @Test
    public void validateReservation2Test() {
        var reservation = new Reservation(null, null);
        reservation.setReservationStatus(ReservationStatus.INVALID);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        var response = this.reservationService.validateReservation(1L);
        Assert.assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Ticket is invalid!");
        Assert.assertEquals(response.getBody().getStatus(), ReservationStatus.INVALID);
    }

    @Test
    public void validateReservation3Test() {
        var reservation = new Reservation(null, null);
        reservation.setReservationStatus(ReservationStatus.CHECKED);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        var response = this.reservationService.validateReservation(1L);
        Assert.assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Ticket is already validated");
        Assert.assertEquals(response.getBody().getStatus(), ReservationStatus.CHECKED);
    }

    @Test
    public void validateReservation4Test() {
        var reservation = new Reservation(null, null);
        reservation.setReservationStatus(ReservationStatus.VALID);
        when(this.reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        var response = this.reservationService.validateReservation(1L);
        Assert.assertEquals(Objects.requireNonNull(response.getBody()).getMessage(), "Ticket was successfully validated");
        Assert.assertEquals(response.getBody().getStatus(), ReservationStatus.CHECKED);
    }
}
