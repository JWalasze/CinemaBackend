package com.rest.cinemaapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rest.cinemaapi.enumerators.SeatSection;
import com.rest.cinemaapi.enumerators.SeatType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seat")
@Getter
@Setter
public class Seat {
    @JsonIgnore
    @Transient
    private final Object lockSeat = new Object();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "layout_column", nullable = false)
    private Integer layoutColumn;

    @Column(name = "layout_row", nullable = false)
    private Integer layoutRow;

    @Column(name = "seat_column", nullable = false)
    private Integer column;

    @Column(name = "seat_row", nullable = false)
    private Integer row;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_section", nullable = false)
    private SeatSection seatSection;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SeatType seatType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "cinema_hall_id", nullable = false, foreignKey = @ForeignKey(name = "hall_fkey"))
    private CinemaHall cinemaHall;

    public Seat() {
    }

    public Seat(
            Integer layoutColumn,
            Integer layoutRow,
            Integer column,
            Integer row,
            CinemaHall cinemaHall,
            SeatSection seatSection
    ) {
        this.layoutColumn = layoutColumn;
        this.layoutRow = layoutRow;
        this.column = column;
        this.row = row;
        this.seatType = SeatType.REGULAR;
        this.cinemaHall = cinemaHall;
        this.seatSection = seatSection;
    }

    @PostPersist
    private void updateCinemaHall() {
        synchronized (lockSeat) {
            this.cinemaHall.setNumberOfSeats(this.getCinemaHall().getNumberOfSeats() + 1);
        }
    }
}
