package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "addresses", itemRelation = "address")
public class AddressDTO {

    private String street;
    private String city;
    private String state;
    private String zip;
}
