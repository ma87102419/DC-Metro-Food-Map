package com.foodie.foodmapapi.repositories;

import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findAllByNearbyStation(Station nearbyStation);
}
