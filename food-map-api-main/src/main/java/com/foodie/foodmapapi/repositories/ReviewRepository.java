package com.foodie.foodmapapi.repositories;

import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findAllByRestaurant(Restaurant restaurant);
}
