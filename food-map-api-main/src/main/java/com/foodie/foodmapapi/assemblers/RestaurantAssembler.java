package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.controllers.AccountController;
import com.foodie.foodmapapi.controllers.RestaurantController;
import com.foodie.foodmapapi.dtos.RestaurantDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RestaurantAssembler implements RepresentationModelAssembler<RestaurantDTO, EntityModel<RestaurantDTO>> {

    @Override
    public EntityModel<RestaurantDTO> toModel(RestaurantDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(RestaurantController.class).getRestaurant(entity.getRestaurantID())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<RestaurantDTO>> toCollectionModel(Iterable<? extends RestaurantDTO> entities) {
        ArrayList<EntityModel<RestaurantDTO>> models = new ArrayList<>();
        entities.forEach(restaurant -> models.add(toModel(restaurant)));
        return CollectionModel.of(models);
    }
}
