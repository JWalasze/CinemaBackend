package com.rest.cinemaapi.services;

import com.rest.cinemaapi.enumerators.ReservationStatus;
import com.rest.cinemaapi.models.*;
import com.rest.cinemaapi.repositories.ProgrammeRepository;
import com.rest.cinemaapi.repositories.ReservationRepository;
import com.rest.cinemaapi.repositories.ReservedSeatRepository;
import com.rest.cinemaapi.repositories.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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

    public Optional<Ticket> makeReservation(ReservationDTO reservation) {
        var programme = this.programmeRepository.findById(reservation.getProgrammeId());

        if (programme.isEmpty()) {
            return Optional.empty();
        }
        System.out.println("==1");

        var newReservation = new Reservation(reservation.getContactData(), programme.get());

        for (Long reservedSeatId : reservation.getReservedSeatsIds()) {
            var seat = this.seatRepository.findById(reservedSeatId);
            var isSeatAlreadyReserved = this.reservedSeatRepository.findReservedSeatBySeatIdAndProgrammeId(reservedSeatId, programme.get().getId());

            if (isSeatAlreadyReserved.isPresent() || seat.isEmpty()) {
                return Optional.empty();
            }

            System.out.println(seat.get().getCinemaHall().getId());
            System.out.println(programme.get().getHall().getId());
            System.out.println("___");
            if (!Objects.equals(seat.get().getCinemaHall().getId(), programme.get().getHall().getId())) {
                return Optional.empty();
            }

            var reservedSeat = new ReservedSeat(newReservation, seat.get(), programme.get());
            newReservation.getReservedSeats().add(reservedSeat);
            System.out.println("==2");
        }

        this.reservationRepository.save(newReservation);
        System.out.println("==3");

        return Optional.of(Ticket.builder().reservationId(newReservation.getId()).build());
    }

    public Optional<ReservationForUsherDTO> getReservation(Long forId) {
        var optionalReservation = this.reservationRepository.findById(forId);

        if (optionalReservation.isEmpty()) {
            return Optional.empty();
        }

        var reservation = optionalReservation.get();
        var reservationForUsher = new ReservationForUsherDTO(reservation);

        return Optional.of(reservationForUsher);
    }

    public ResponseEntity<ReservationStatusDTO> validateReservation(Long forId) {
        var optionalReservation = this.reservationRepository.findById(forId);
        if (optionalReservation.isEmpty()) {
            return ResponseEntity.ok(new ReservationStatusDTO(forId, ReservationStatus.NOT_EXISTS, "Ticket does not exist!"));
        }

        var reservation = optionalReservation.get();

        if (reservation.getReservationStatus() == ReservationStatus.INVALID ||
                reservation.getReservationStatus() == ReservationStatus.NOT_EXISTS) {
            return ResponseEntity.ok(new ReservationStatusDTO(forId, ReservationStatus.INVALID, "Ticket is invalid!"));
        }

        if (reservation.getReservationStatus() == ReservationStatus.CHECKED) {
            return ResponseEntity.ok(new ReservationStatusDTO(forId, ReservationStatus.CHECKED, "Ticket is already validated"));
        }

        reservation.setReservationStatus(ReservationStatus.CHECKED);
        this.reservationRepository.save(reservation);

        return ResponseEntity.ok(new ReservationStatusDTO(forId, ReservationStatus.CHECKED, "Ticket was successfully validated"));
    }
}
