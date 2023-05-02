package com.foodie.foodmapapi.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@Entity
@Table(name = "role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(collectionRelation = "roles", itemRelation = "role")
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name="role_value")
    private RoleEnum value;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;
}
