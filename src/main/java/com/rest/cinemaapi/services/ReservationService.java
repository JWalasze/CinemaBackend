package com.rest.cinemaapi.services;

import com.rest.cinemaapi.models.Reservation;
import com.rest.cinemaapi.models.ReservationDTO;
import com.rest.cinemaapi.models.ReservedSeat;
import com.rest.cinemaapi.models.Ticket;
import com.rest.cinemaapi.repositories.ProgrammeRepository;
import com.rest.cinemaapi.repositories.ReservationRepository;
import com.rest.cinemaapi.repositories.ReservedSeatRepository;
import com.rest.cinemaapi.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    private final ProgrammeRepository programmeRepository;

    private final SeatRepository seatRepository;

    private final ReservedSeatRepository reservedSeatRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository,
                              ProgrammeRepository programmeRepository,
                              SeatRepository seatRepository,
                              ReservedSeatRepository reservedSeatRepository, ReservedSeatRepository reservedSeatRepository1) {
        this.reservationRepository = reservationRepository;
        this.programmeRepository = programmeRepository;
        this.seatRepository = seatRepository;
        this.reservedSeatRepository = reservedSeatRepository1;
    }

    public List<Ticket> makeReservation(ReservationDTO reservation) {
        var programme = this.programmeRepository.findById(reservation.getProgrammeId());

        if (programme.isEmpty()) {
            return null;
        }

        var newReservation = new Reservation(reservation.getContactData(), programme.get());

        for (Long reservedSeatId : reservation.getReservedSeatsIds()) {
            var seat = this.seatRepository.findById(reservedSeatId);
            var isSeatAlreadyReserved = this.reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(reservedSeatId, programme.get().getId());

            if (isSeatAlreadyReserved.isPresent() || seat.isEmpty()) {
                return null;
            }

            System.out.println(seat.get().getCinemaHall().getId());
            System.out.println(programme.get().getHall().getId());
            System.out.println("___");
            if (!Objects.equals(seat.get().getCinemaHall().getId(), programme.get().getHall().getId())) {
                return null;
            }

            var reservedSeat = new ReservedSeat(newReservation, seat.get(), programme.get());
            newReservation.getReservedSeats().add(reservedSeat);
        }

        this.reservationRepository.save(newReservation);

        return null;
    }
}
