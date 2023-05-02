package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Review;

import java.util.List;
import java.util.Set;

public interface RestaurantService {

    List<Restaurant> getNearbyRestaurants(Integer stationID);

    Restaurant getRestaurant(Integer restaurantID);

    List<Review> getRestaurantReviews(Integer restaurantID);

    Set<Restaurant> getFavoriteRestaurants(String username);

    Restaurant addFavoriteRestaurant(String username, Integer restaurantID);

    Restaurant removeFavoriteRestaurant(String username, Integer restaurantID);
}
