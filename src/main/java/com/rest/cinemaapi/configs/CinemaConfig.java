package com.rest.cinemaapi.configs;

import com.rest.cinemaapi.enumerators.*;
import com.rest.cinemaapi.models.*;
import com.rest.cinemaapi.repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CinemaConfig {
    @PersistenceContext
    EntityManager entityManager;

    @Bean
    CommandLineRunner commandLineRunner(
            SeatRepository seatRepository,
            CinemaHallRepository cinemaHallRepository,
            CinemaRepository cinemaRepository,
            ProgrammeRepository programmeRepository,
            FilmRepository filmRepository,
            ReservationRepository reservationRepository,
            ReservedSeatRepository reservedSeatRepository,
            UsherRepository usherRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            var address = new Address("Wrocław", "58-303", "Robotnicza", "96");

            var cinema1 = new Cinema("Wrocław Magnolia Park", address);
            var cinema2 = new Cinema("Warszawa Złote Tarasy", address);

            var cinemaHall1 = new CinemaHall("3B", HallType.STANDARD, cinema2);
            var cinemaHall2 = new CinemaHall("4A", HallType.SCREEN_4DX, cinema1);

            var seat1 = new Seat(2, 3, 4, 5, cinemaHall1, SeatSection.A);
            var seat2 = new ForDisabledSeat(1, 3, 2, 3, cinemaHall2, SeatSection.B);
            var seat3 = new Seat(2, 5, 2, 6, cinemaHall1, SeatSection.A);
            var seat4 = new DoubleSeat(7, 7, 7, 8, cinemaHall1, 7, 8, 7, 9, SeatSection.C);
            var doubleSeat5 = new DoubleSeat(4, 5, 4, 5, cinemaHall1, 5, 6, 5, 6, SeatSection.C);

            cinemaHall1.getSeats().addAll(Arrays.asList(seat1, doubleSeat5, seat3, seat4, seat1));
            cinemaHall2.getSeats().add(seat2);

            cinema1.getHalls().add(cinemaHall2);
            cinema2.getHalls().add(cinemaHall1);

            var cinemaHall3 = new CinemaHall("Sala Główna", HallType.STANDARD, cinema1);
            cinema1.getHalls().add(cinemaHall3);

            //Section B disabled row
            var dis1 = new ForDisabledSeat(0, 0, 1, 1, cinemaHall3, SeatSection.B);
            var dis2 = new ForDisabledSeat(1, 0, 2, 1, cinemaHall3, SeatSection.B);
            var dis3 = new ForDisabledSeat(2, 0, 3, 1, cinemaHall3, SeatSection.B);
            //section B regular row
            var s1 = new Seat(0, 1, 1, 2, cinemaHall3, SeatSection.B);
            var s2 = new Seat(1, 1, 2, 2, cinemaHall3, SeatSection.B);
            var s3 = new Seat(2, 1, 3, 2, cinemaHall3, SeatSection.B);

            cinemaHall3.getSeats().addAll(List.of(dis1, dis2, dis3));
            cinemaHall3.getSeats().addAll(List.of(s1, s2, s3));

            //Section A regular
            for (int row = 0; row < 4; row++) {
                for (int col = 4; col <= 8; col++) {
                    cinemaHall3.getSeats().add(new Seat(col, row, col-3, row+1, cinemaHall3, SeatSection.A));
                }
            }

            //Section C 2 * double + 1 * regular
            var d1 = new DoubleSeat(4, 5, 1, 5, cinemaHall3, 5, 5, 2, 5, SeatSection.C);
            var d12 = new DoubleSeat(5, 5, 2, 5, cinemaHall3, 4, 5, 1, 5, SeatSection.C);
            var d2 = new DoubleSeat(6, 5, 3, 5, cinemaHall3, 7, 5, 4, 5, SeatSection.C);
            var d22 = new DoubleSeat(7, 5, 4, 5, cinemaHall3, 6, 5, 3, 5, SeatSection.C);
            var s = new Seat(8, 5, 5, 5, cinemaHall3, SeatSection.C);

            cinemaHall3.getSeats().addAll(List.of(d1, d12, d2, d22, s));

            cinemaRepository.save(cinema1);
            cinemaRepository.save(cinema2);

            String mockImgUrl = "https://via.placeholder.com/150x200";

            var film1 = new Film("Szybcy i Wściekli", "x", "x", "x", BigDecimal.valueOf(43.682), LocalDate.now(), LocalTime.now(), AgeLimit.PG18, Genre.ACTION, mockImgUrl, FilmLanguageType.DUBBING, FilmScreenType.SCREEN_2D, FilmType.SCREEN_X);
            var film2 = new Film("Jagodno", "y", "y", "y", BigDecimal.valueOf(23.45), LocalDate.now(), LocalTime.now(), AgeLimit.PG18, Genre.DRAMA, mockImgUrl, FilmLanguageType.DUBBING, FilmScreenType.SCREEN_2D, FilmType.SCREEN_X);
            var film3 = new Film("Harry Potter", "y", "y", "y", BigDecimal.valueOf(23.45), LocalDate.now(), LocalTime.now(), AgeLimit.PG18, Genre.COMEDY, "https://img.posterstore.com/zoom/wb0003-8harrypotter-half-bloodprince50x70.jpg", FilmLanguageType.DUBBING, FilmScreenType.SCREEN_3D, FilmType.DOLBY_ATMOS);
            var film4 = new Film("Indiana Jones", "y", "y", "y", BigDecimal.valueOf(23.45), LocalDate.now(), LocalTime.now(), AgeLimit.PG18, Genre.ACTION, "https://pbs.twimg.com/media/FxbxEb9aEAAqpOS.jpg:small", FilmLanguageType.DUBBING, FilmScreenType.SCREEN_3D, FilmType.HALL_4DX);
            var film5 = new Film("Top", "y", "y", "y", BigDecimal.valueOf(23.45), LocalDate.now(), LocalTime.now(), AgeLimit.PG18, Genre.ANIMATED, mockImgUrl, FilmLanguageType.DUBBING, FilmScreenType.SCREEN_2D, FilmType.STANDARD);

            filmRepository.saveAll(List.of(film1, film2, film3, film4, film5)); //DAC TEST ŻE DZIAla SORTING
            
            var now = LocalDateTime.now();
            var tomorrow = now.plusDays(1);
            var nowPlus1h = now.plusHours(1);

            var programme1 = new Programme(LocalDateTime.now(), film1, cinemaHall1);
            var programme2 = new Programme(LocalDateTime.now(), film2, cinemaHall2);
            var programme4 = new Programme(LocalDateTime.now(), film1, cinemaHall2);
            var programme3 = new Programme(LocalDateTime.of(2001, 12, 3, 12, 30), film1, cinemaHall2);

            var prog5 = new Programme(LocalDateTime.now(), film3, cinemaHall1);
            var prog6 = new Programme(LocalDateTime.now(), film4, cinemaHall2);
            var prog7 = new Programme(LocalDateTime.now(), film5, cinemaHall2);

            var prog8 = new Programme(nowPlus1h, film5, cinemaHall3);

            var prog9 = new Programme(tomorrow, film4, cinemaHall3);

            programmeRepository.saveAll(List.of(programme1, programme2, programme3, programme4, prog5, prog6, prog7, prog8, prog9));

            var contact = new ContactData("Kuba", "W", "500", "500@");

            var reservation1 = new Reservation(contact, programme1);
            var reservation2 = new Reservation(contact, programme1);

            var seatRes1 = new ReservedSeat(reservation1, seat1, programme1);
            var seatRes2 = new ReservedSeat(reservation1, seat4, programme1);

            var seatRes3 = new ReservedSeat(reservation2, seat3, programme1);
            var seatRes4 = new ReservedSeat(reservation2, seat1, programme2);

            reservation1.getReservedSeats().addAll(List.of(seatRes1, seatRes2));
            reservation2.getReservedSeats().addAll(List.of(seatRes3, seatRes4));

            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.delete(reservation1);

            var usher = new Usher("Kuba", "Walaszek", cinema1);
            usher.setEmail("walaszekjakub1234@mail.com");
            usher.setPassword(passwordEncoder.encode("haslo1234"));
            usher.setRole(Role.USHER);
            usherRepository.save(usher);
        };
    }
}
