package com.foodie.foodmapapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "metro_station")
public class Station {

    @Id
    @Column(name = "station_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stationID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address_street", nullable = false)
    private String addressStreet;

    @Column(name = "address_city", nullable = false)
    private String addressCity;

    @Column(name = "address_state", nullable = false)
    private String addressState;

    @Column(name = "address_zip", nullable = false)
    private String addressZip;

    @OneToMany(mappedBy = "station")
    @EqualsAndHashCode.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    private Set<Platform> platforms = new HashSet<>();
}
