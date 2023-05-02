package com.foodie.foodmapapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.foodie.foodmapapi.models.RoleEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "accounts", itemRelation = "account")
public class AccountDTO {

    private String username;
    private String password;
    private String email;
    private String name;
    private Set<RoleEnum> roles;
    private Boolean locked;
    private Boolean disabled;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private OffsetDateTime accountExpiration;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private OffsetDateTime passwordExpiration;
}
