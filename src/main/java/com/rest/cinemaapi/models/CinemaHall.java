package com.rest.cinemaapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rest.cinemaapi.enumerators.HallType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "cinema-halls-with-all-seats",
        attributeNodes = {@NamedAttributeNode("seats")}
)
@Entity
@Table(name = "cinema_hall")
@Getter
@Setter
public class CinemaHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String hallName;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "hall_type",
            nullable = false
    )
    private HallType hallType;

    @OneToMany(
            mappedBy = "cinemaHall",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    private List<Seat> seats;

    @Transient
    private Integer numberOfSeats;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(
            name = "cinema_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "cinema_fkey")
    )
    private Cinema cinema;

    public CinemaHall() {
        this.seats = new ArrayList<>();
        this.numberOfSeats = 0;
    }

    public CinemaHall(
            String hallName,
            HallType hallType,
            Cinema cinema
    ) {
        this.hallName = hallName;
        this.hallType = hallType;
        this.cinema = cinema;
        this.seats = new ArrayList<>();
        this.numberOfSeats = 0;
    }

    public CinemaHall(CinemaHall cinemaHall) {
        this.id = cinemaHall.getId();
        this.hallName = cinemaHall.getHallName();
        this.hallType = cinemaHall.getHallType();
        this.cinema = cinemaHall.getCinema();
        this.seats = new ArrayList<>();
        this.numberOfSeats = cinemaHall.getNumberOfSeats();
    }

    @PostLoad
    private void postLoadMethod() {
        System.out.println("TUTAJ");
        this.numberOfSeats = this.seats.size();
    }
}
