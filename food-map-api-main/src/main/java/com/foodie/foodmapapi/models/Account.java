package com.foodie.foodmapapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(name = "favorite_restaurant",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Restaurant> favoriteRestaurants = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name="username"),
            inverseJoinColumns = @JoinColumn(name="role_value"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @Column(name = "disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "account_expiration", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime accountExpiration;

    @Column(name = "password_expiration", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime passwordExpiration;
}
