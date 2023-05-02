package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.controllers.StationController;
import com.foodie.foodmapapi.dtos.StationDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StationAssembler implements RepresentationModelAssembler<StationDTO, EntityModel<StationDTO>> {

    @Override
    public EntityModel<StationDTO> toModel(StationDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StationController.class).getStation(entity.getStationID().toString())).withSelfRel());
    }
}
