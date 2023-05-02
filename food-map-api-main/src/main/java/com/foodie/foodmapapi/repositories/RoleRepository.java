package com.foodie.foodmapapi.repositories;

import com.foodie.foodmapapi.models.Role;
import com.foodie.foodmapapi.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByValue(RoleEnum value);
}
