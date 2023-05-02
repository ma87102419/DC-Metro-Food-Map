package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Account;
import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Review;
import com.foodie.foodmapapi.repositories.AccountRepository;
import com.foodie.foodmapapi.repositories.RestaurantRepository;
import com.foodie.foodmapapi.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final StationService stationService;
    private final ErrorMessagesProperties errorMessages;

    @Override
    public List<Restaurant> getNearbyRestaurants(Integer stationID) {
        return restaurantRepository.findAllByNearbyStation(stationService.getStation(stationID));
    }

    @Override
    public Restaurant getRestaurant(Integer restaurantID) {
        return restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByRestaurantID() + restaurantID));
    }

    @Override
    public List<Review> getRestaurantReviews(Integer restaurantID) {
        return reviewRepository.findAllByRestaurant(getRestaurant(restaurantID));
    }

    @Override
    public Set<Restaurant> getFavoriteRestaurants(String username) {
        return accountService.getAccount(username).getFavoriteRestaurants();
    }

    @Override
    public Restaurant addFavoriteRestaurant(String username, Integer restaurantID) {
        Account account = accountService.getAccount(username);
        Restaurant restaurant = getRestaurant(restaurantID);
        account.getFavoriteRestaurants().add(restaurant);
        accountRepository.save(account);
        return restaurant;
    }

    @Override
    public Restaurant removeFavoriteRestaurant(String username, Integer restaurantID) {
        Account account = accountService.getAccount(username);
        Restaurant restaurant = getRestaurant(restaurantID);
        if (account.getFavoriteRestaurants().remove(restaurant)) {
            accountRepository.save(account);
            return restaurant;
        }
        else{
            throw new NotFoundException(errorMessages.getNotFoundByRestaurantID() + restaurantID);
        }
    }
}
