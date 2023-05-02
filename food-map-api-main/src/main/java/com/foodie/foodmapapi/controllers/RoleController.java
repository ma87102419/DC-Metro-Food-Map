package com.foodie.foodmapapi.controllers;

import com.foodie.foodmapapi.assemblers.RoleAssembler;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.models.Role;
import com.foodie.foodmapapi.models.RoleEnum;
import com.foodie.foodmapapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final RoleService roleService;
    private final RoleAssembler roleAssembler;

    @GetMapping("/{roleValue}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<Role>> getRole(@PathVariable("roleValue") RoleEnum roleValue) {
        try {
            logger.info("RoleController.getRole({})", roleValue);
            Role role = roleService.getRoleByValue(roleValue);
            EntityModel<Role> roleModel = roleAssembler.toModel(role);
            return new ResponseEntity<>(roleModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CollectionModel<EntityModel<Role>>> getAllRoles() {
        try {
            logger.info("RoleController.getAllRoles()");
            List<Role> roles = roleService.getAllRoles();
            CollectionModel<EntityModel<Role>> roleModels = roleAssembler.toCollectionModel(roles);
            return new ResponseEntity<>(roleModels, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }
}
