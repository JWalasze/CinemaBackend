package com.rest.cinemaapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "cinema-with-all-halls",
        attributeNodes = {@NamedAttributeNode("halls")}
)
@Entity
@Table(name = "cinema")
@Getter
@Setter
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Embedded
    @AttributeOverrides(
            {@AttributeOverride(
                    name = "city",
                    column = @Column(
                            name = "city",
                            nullable = false
                    )
            ), @AttributeOverride(
                    name = "postalCode",
                    column = @Column(
                            name = "postal_code",
                            nullable = false
                    )
            ), @AttributeOverride(
                    name = "streetName",
                    column = @Column(
                            name = "street_name",
                            nullable = false
                    )
            ), @AttributeOverride(
                    name = "streetNumber",
                    column = @Column(
                            name = "street_number",
                            nullable = false
                    )
            )}
    )
    private Address address;

    @JsonIgnore
    @OneToMany(
            mappedBy = "cinema",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    private List<CinemaHall> halls;

    @JsonIgnore
    @OneToMany(
            mappedBy = "cinema",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY
    )
    private List<Usher> ushers;

    public Cinema() {
        this.halls = new ArrayList<>();
        this.ushers = new ArrayList<>();
    }

    public Cinema(
            String name,
            Address address
    ) {
        this.name = name;
        this.address = address;
        this.halls = new ArrayList<>();
        this.ushers = new ArrayList<>();
    }
}
