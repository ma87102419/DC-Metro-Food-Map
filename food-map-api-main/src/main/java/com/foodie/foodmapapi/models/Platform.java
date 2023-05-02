package com.foodie.foodmapapi.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(Platform.PlatformID.class)
@Table(name = "metro_station_platform")
public class Platform {
    @Id
    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Id
    @ManyToOne
    @JoinColumn(name = "line_code", nullable = false)
    @ToString.Exclude
    private Line line;

    @Column(name = "station_code", nullable = false)
    private String stationCode;

    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlatformID implements Serializable {
        private Station station;
        private Line line;
    }
}
