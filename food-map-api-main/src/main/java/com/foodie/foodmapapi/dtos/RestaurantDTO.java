package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "restaurants", itemRelation = "restaurant")
public class RestaurantDTO {

    private Integer restaurantID;
    private String name;
    private String thirdPartyID;
    private String thirdPartySource;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Double starRating;
    private Integer priceRating;
    private Double latitude;
    private Double longitude;
    private AddressDTO address;
}
