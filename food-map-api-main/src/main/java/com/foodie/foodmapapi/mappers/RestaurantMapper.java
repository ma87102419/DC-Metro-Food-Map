package com.foodie.foodmapapi.mappers;

import com.foodie.foodmapapi.dtos.AddressDTO;
import com.foodie.foodmapapi.dtos.RestaurantDTO;
import com.foodie.foodmapapi.models.Restaurant;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    public RestaurantDTO map(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setRestaurantID(restaurant.getRestaurantID());
        dto.setName(restaurant.getName());
        dto.setThirdPartyID(restaurant.getThirdPartyID());
        dto.setThirdPartySource(restaurant.getThirdPartySource());
        dto.setOpenTime(restaurant.getOpenTime());
        dto.setCloseTime(restaurant.getCloseTime());
        dto.setStarRating(restaurant.getStarRating());
        dto.setPriceRating(restaurant.getPriceRating());
        dto.setLatitude(restaurant.getLatitude());
        dto.setLongitude(restaurant.getLongitude());
        AddressDTO address = new AddressDTO();
        address.setStreet(restaurant.getAddressStreet());
        address.setCity(restaurant.getAddressCity());
        address.setState(restaurant.getAddressState());
        address.setZip(restaurant.getAddressZip());
        dto.setAddress(address);
        return dto;
    }
}
