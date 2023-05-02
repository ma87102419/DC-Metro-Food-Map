package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "lines", itemRelation = "line")
public class LineDTO {

    private String lineCode;
    private String name;
    private Set<EntityModel<StationDTO>> stations;
}
