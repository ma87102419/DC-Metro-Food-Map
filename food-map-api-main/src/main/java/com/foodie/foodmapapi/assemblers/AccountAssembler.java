package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.controllers.AccountController;
import com.foodie.foodmapapi.dtos.AccountDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AccountAssembler implements RepresentationModelAssembler<AccountDTO, EntityModel<AccountDTO>> {

    @Override
    public EntityModel<AccountDTO> toModel(AccountDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(AccountController.class).getAccount(entity.getUsername(), null)).withSelfRel(),
                linkTo(methodOn(AccountController.class).getFavoriteRestaurants(entity.getUsername())).withRel("favorites"));
    }
}
