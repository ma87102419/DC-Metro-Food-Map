package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "stations", itemRelation = "station")
public class StationDTO {

    private Integer stationID;
    private String name;
    private Double latitude;
    private Double longitude;
    private AddressDTO address;
    private Set<PlatformDTO> platforms;
}
