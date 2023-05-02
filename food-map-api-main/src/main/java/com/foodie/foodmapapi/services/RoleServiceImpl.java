package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Role;
import com.foodie.foodmapapi.models.RoleEnum;
import com.foodie.foodmapapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ErrorMessagesProperties errorMessages;

    @Override
    public Role getRoleByValue(RoleEnum value) {
        return roleRepository.findByValue(value)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByRoleValue() + value));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public boolean hasRole(RoleEnum roleEnum, UserDetails user) {
        return user.getAuthorities().contains(new SimpleGrantedAuthority(roleEnum.name()));
    }
}
