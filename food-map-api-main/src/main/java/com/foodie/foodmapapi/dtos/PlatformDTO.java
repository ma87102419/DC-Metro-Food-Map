package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "stationPlatforms", itemRelation = "stationPlatform")
public class PlatformDTO {

    private String stationCode;
    private LineDTO line;
}
