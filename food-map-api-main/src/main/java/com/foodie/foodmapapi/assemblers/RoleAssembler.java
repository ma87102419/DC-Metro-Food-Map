package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.controllers.RoleController;
import com.foodie.foodmapapi.models.Role;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RoleAssembler implements RepresentationModelAssembler<Role, EntityModel<Role>> {


    @Override
    public EntityModel<Role> toModel(Role entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(RoleController.class).getRole(entity.getValue())).withSelfRel());
    }

    @Override
    public CollectionModel<EntityModel<Role>> toCollectionModel(Iterable<? extends Role> entities) {
        ArrayList<EntityModel<Role>> models = new ArrayList<>();
        entities.forEach(role -> models.add(toModel(role)));
        return CollectionModel.of(models,
                linkTo(methodOn(RoleController.class).getAllRoles()).withSelfRel());
    }
}
