package com.foodie.foodmapapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "metro_line")
public class Line {

    @Id
    @Column(name = "line_code", nullable = false)
    private String lineCode;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "line")
    @EqualsAndHashCode.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Platform> platforms = new HashSet<>();
}
