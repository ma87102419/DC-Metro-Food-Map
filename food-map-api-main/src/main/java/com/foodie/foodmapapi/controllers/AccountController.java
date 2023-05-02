package com.foodie.foodmapapi.controllers;

import com.foodie.foodmapapi.assemblers.AccountAssembler;
import com.foodie.foodmapapi.assemblers.RestaurantAssembler;
import com.foodie.foodmapapi.dtos.AccountDTO;
import com.foodie.foodmapapi.dtos.RestaurantDTO;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.mappers.AccountMapper;
import com.foodie.foodmapapi.mappers.RestaurantMapper;
import com.foodie.foodmapapi.models.Account;
import com.foodie.foodmapapi.models.Restaurant;
import com.foodie.foodmapapi.models.RoleEnum;
import com.foodie.foodmapapi.services.AccountService;
import com.foodie.foodmapapi.services.RestaurantService;
import com.foodie.foodmapapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountAssembler accountAssembler;
    private final RoleService roleService;
    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantAssembler restaurantAssembler;

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<EntityModel<AccountDTO>> getAccount(@PathVariable("username") String username, Authentication authentication) {
        try {
            UserDetails requester = (UserDetails) authentication.getPrincipal();
            logger.info("AccountController.getAccount({}, {})", username, requester.getUsername());
            Account account = accountService.getAccount(username);

            // Don't expose private info to the wrong requester
            AccountDTO accountDTO;
            if (roleService.hasRole(RoleEnum.ROLE_ADMIN, requester)) {
                accountDTO = accountMapper.mapAuthInfo(account);
            }
            else if (requester.getUsername().equals(username)) {
                accountDTO = accountMapper.mapPrivateInfo(account);
            }
            else {
                accountDTO = accountMapper.mapPublicInfo(account);
            }

            EntityModel<AccountDTO> accountModel = accountAssembler.toModel(accountDTO);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or ( hasRole('USER') and #username == authentication.principal.username )")
    public ResponseEntity<EntityModel<AccountDTO>> deleteAccount(@PathVariable("username") String username) {
        try {
            logger.info("AccountController.deleteAccount({})", username);
            Account account = accountService.removeAccount(username);
            AccountDTO accountDTO = accountMapper.mapPrivateInfo(account);
            EntityModel<AccountDTO> accountModel = accountAssembler.toModel(accountDTO);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PatchMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<EntityModel<AccountDTO>> patchAccount(@PathVariable("username") String username, @RequestBody AccountDTO accountRequest) {
        try {
            logger.info("AccountController.patchAccount({})", username);
            Account account = accountService.updateAccount(username, accountMapper.mapPrivateInfo(accountRequest), false);
            AccountDTO accountDTO = accountMapper.mapPrivateInfo(account);
            EntityModel<AccountDTO> accountModel = accountAssembler.toModel(accountDTO);
            return new ResponseEntity<>(accountModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @GetMapping("/{username}/favorites")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<CollectionModel<EntityModel<RestaurantDTO>>> getFavoriteRestaurants(@PathVariable("username") String username) {
        try {
            logger.info("AccountController.getFavoriteRestaurants({})", username);
            Set<Restaurant> restaurants = restaurantService.getFavoriteRestaurants(username);
            Set<RestaurantDTO> restaurantDTOs = restaurants.stream().map(restaurantMapper::map).collect(Collectors.toSet());
            CollectionModel<EntityModel<RestaurantDTO>> restaurantModels = restaurantAssembler.toCollectionModel(restaurantDTOs);
            restaurantModels.add(linkTo(methodOn(this.getClass()).getFavoriteRestaurants(username)).withSelfRel());
            return new ResponseEntity<>(restaurantModels, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PostMapping("/{username}/favorites")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<EntityModel<RestaurantDTO>> postFavoriteRestaurant(@PathVariable("username") String username, @RequestBody RestaurantDTO restaurantRequest) {
        try {
            logger.info("AccountController.postFavoriteRestaurant({}, {})", username, restaurantRequest.getRestaurantID());
            Restaurant restaurant = restaurantService.addFavoriteRestaurant(username, restaurantRequest.getRestaurantID());
            RestaurantDTO restaurantDTO = restaurantMapper.map(restaurant);
            EntityModel<RestaurantDTO> restaurantModel = restaurantAssembler.toModel(restaurantDTO);
            restaurantModel.add(linkTo(methodOn(this.getClass()).getFavoriteRestaurants(username)).withRel("favorites"));
            return new ResponseEntity<>(restaurantModel, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @DeleteMapping("/{username}/favorites/{restaurantID}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<EntityModel<RestaurantDTO>> deleteFavoriteRestaurant(@PathVariable("username") String username, @PathVariable("restaurantID") Integer restaurantID) {
        try {
            logger.info("AccountController.deleteFavoriteRestaurant({}, {})", username, restaurantID);
            Restaurant restaurant = restaurantService.removeFavoriteRestaurant(username, restaurantID);
            RestaurantDTO restaurantDTO = restaurantMapper.map(restaurant);
            EntityModel<RestaurantDTO> restaurantModel = restaurantAssembler.toModel(restaurantDTO);
            restaurantModel.add(linkTo(methodOn(this.getClass()).getFavoriteRestaurants(username)).withRel("favorites"));
            return new ResponseEntity<>(restaurantModel, HttpStatus.OK);
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
