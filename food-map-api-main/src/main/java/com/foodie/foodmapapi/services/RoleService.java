package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.models.Role;
import com.foodie.foodmapapi.models.RoleEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface RoleService {

    Role getRoleByValue(RoleEnum value);

    List<Role> getAllRoles();

    boolean hasRole(RoleEnum roleEnum, UserDetails user);
}
