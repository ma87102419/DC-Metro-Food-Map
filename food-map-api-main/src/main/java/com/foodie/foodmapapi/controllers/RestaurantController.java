package com.foodie.foodmapapi.controllers;

import com.foodie.foodmapapi.assemblers.RestaurantAssembler;
import com.foodie.foodmapapi.assemblers.ReviewAssembler;
import com.foodie.foodmapapi.dtos.RestaurantDTO;
import com.foodie.foodmapapi.dtos.ReviewDTO;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.mappers.RestaurantMapper;
import com.foodie.foodmapapi.mappers.ReviewMapper;
import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Review;
import com.foodie.foodmapapi.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantAssembler restaurantAssembler;
    private final ReviewMapper reviewMapper;
    private final ReviewAssembler reviewAssembler;

    @GetMapping("/{restaurantID}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<EntityModel<RestaurantDTO>> getRestaurant(@PathVariable("restaurantID") Integer restaurantID) {
        try {
            logger.info("RestaurantController.getRestaurant({})", restaurantID);
            Restaurant restaurant = restaurantService.getRestaurant(restaurantID);
            RestaurantDTO restaurantDTO = restaurantMapper.map(restaurant);
            EntityModel<RestaurantDTO> restaurantModel = restaurantAssembler.toModel(restaurantDTO);
            return new ResponseEntity<>(restaurantModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/{restaurantID}/reviews")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CollectionModel<EntityModel<ReviewDTO>>> getReviews(@PathVariable("restaurantID") Integer restaurantID) {
        try {
            logger.info("RestaurantController.getReviews({})", restaurantID);
            List<Review> reviews = restaurantService.getRestaurantReviews(restaurantID);
            List<ReviewDTO> reviewDTOs = reviews.stream().map(reviewMapper::map).toList();
            CollectionModel<EntityModel<ReviewDTO>> reviewModels = reviewAssembler.toCollectionModel(reviewDTOs);
            reviewModels.add(linkTo(methodOn(this.getClass()).getReviews(restaurantID)).withSelfRel());
            return new ResponseEntity<>(reviewModels, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
