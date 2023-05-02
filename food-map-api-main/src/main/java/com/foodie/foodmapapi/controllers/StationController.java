package com.foodie.foodmapapi.controllers;


import com.foodie.foodmapapi.assemblers.RestaurantAssembler;
import com.foodie.foodmapapi.assemblers.StationAssembler;
import com.foodie.foodmapapi.dtos.RestaurantDTO;
import com.foodie.foodmapapi.dtos.StationDTO;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.mappers.MetroMapper;
import com.foodie.foodmapapi.mappers.RestaurantMapper;
import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.Station;
import com.foodie.foodmapapi.services.RestaurantService;
import com.foodie.foodmapapi.services.StationService;
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
@RequestMapping("/api/stations")
@RequiredArgsConstructor
public class StationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final StationService stationService;
    private final MetroMapper metroMapper;
    private final StationAssembler stationAssembler;
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantAssembler restaurantAssembler;

    @GetMapping("/{stationIDorCode}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<EntityModel<StationDTO>> getStation(@PathVariable("stationIDorCode") String stationIDorCode) {
        try {
            logger.info("StationController.getStation({})", stationIDorCode);
            Station station = stationIDorCode.matches("[0-9]+") ?
                    stationService.getStation(Integer.parseInt(stationIDorCode)) :
                    stationService.getStation(stationIDorCode);
            StationDTO stationDTO = metroMapper.mapStation(station);
            EntityModel<StationDTO> stationModel = stationAssembler.toModel(stationDTO);
            return new ResponseEntity<>(stationModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/{stationID}/restaurants")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<CollectionModel<EntityModel<RestaurantDTO>>> getNearbyRestaurants(@PathVariable("stationID") Integer stationID) {
        try {
            logger.info("StationController.getNearbyRestaurants({})", stationID);
            List<Restaurant> restaurants = restaurantService.getNearbyRestaurants(stationID);
            List<RestaurantDTO> restaurantDTOs = restaurants.stream().map(restaurantMapper::map).toList();
            CollectionModel<EntityModel<RestaurantDTO>> restaurantModels = restaurantAssembler.toCollectionModel(restaurantDTOs);
            restaurantModels.add(linkTo(methodOn(this.getClass()).getNearbyRestaurants(stationID)).withSelfRel());
            return new ResponseEntity<>(restaurantModels, HttpStatus.OK);
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
