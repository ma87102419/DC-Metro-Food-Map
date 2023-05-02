package com.foodie.foodmapapi.assemblers;

import com.foodie.foodmapapi.dtos.ReviewDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ReviewAssembler implements RepresentationModelAssembler<ReviewDTO, EntityModel<ReviewDTO>> {
    @Override
    public EntityModel<ReviewDTO> toModel(ReviewDTO entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<ReviewDTO>> toCollectionModel(Iterable<? extends ReviewDTO> entities) {
        ArrayList<EntityModel<ReviewDTO>> models = new ArrayList<>();
        entities.forEach(review -> models.add(toModel(review)));
        return CollectionModel.of(models);
    }
}
