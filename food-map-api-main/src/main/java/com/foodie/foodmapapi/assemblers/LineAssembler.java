package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.controllers.LineController;
import com.foodie.foodmapapi.dtos.LineDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LineAssembler implements RepresentationModelAssembler<LineDTO, EntityModel<LineDTO>> {

    @Override
    public EntityModel<LineDTO> toModel(LineDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(LineController.class).getLine(entity.getLineCode())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<LineDTO>> toCollectionModel(Iterable<? extends LineDTO> entities) {
        ArrayList<EntityModel<LineDTO>> models = new ArrayList<>();
        entities.forEach(line -> models.add(toModel(line)));
        return CollectionModel.of(models,
                linkTo(methodOn(LineController.class).getAllLines()).withSelfRel());
    }
}
