package com.foodie.foodmapapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restaurantID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "third_party_id", nullable = false)
    private String thirdPartyID;

    @Column(name = "third_party_source", nullable = false)
    private String thirdPartySource;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "star_rating")
    private Double starRating;

    @Column(name = "price_rating")
    private Integer priceRating;

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

    @ManyToOne
    @JoinColumn(name = "nearby_station_id", nullable = false)
    private Station nearbyStation;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<Review> reviews = new HashSet<>();
}
