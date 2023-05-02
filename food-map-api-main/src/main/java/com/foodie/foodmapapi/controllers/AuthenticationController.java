package com.foodie.foodmapapi.controllers;

import com.foodie.foodmapapi.assemblers.AccountAssembler;
import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.dtos.*;
import com.foodie.foodmapapi.exceptions.InvalidParameterException;
import com.foodie.foodmapapi.exceptions.ServiceException;
import com.foodie.foodmapapi.mappers.AccountMapper;
import com.foodie.foodmapapi.models.Account;
import com.foodie.foodmapapi.models.RoleEnum;
import com.foodie.foodmapapi.services.AccountService;
import com.foodie.foodmapapi.services.JwtAuthTokenService;
import com.foodie.foodmapapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AccountService accountService;
    private final AccountMapper accountMapper;
    private final AccountAssembler accountAssembler;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtAuthTokenService jwtAuthTokenService;
    private final ErrorMessagesProperties errorMessages;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            logger.info("AuthenticationController.login({})", loginRequest.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = jwtAuthTokenService.generateToken(userDetails);

            LoginResponse response = new LoginResponse();
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (ServiceException e) {
            throw e;
        }
        catch (UsernameNotFoundException e) {
            throw new InvalidParameterException(errorMessages.getNotFoundByUsername() + loginRequest.getUsername());
        }
        catch (BadCredentialsException e) {
            throw new InvalidParameterException(errorMessages.getBadCredentials());
        }
        catch (AuthenticationException e) {
            throw new InvalidParameterException(errorMessages.getFailedAuthentication());
        }
        catch (Exception e) {
            logger.error("Some Error: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        try {
            logger.info("AuthenticationController.signup({})", signupRequest.getUsername());
            Account account = new Account();
            account.setUsername(signupRequest.getUsername());
            account.setPassword(signupRequest.getPassword());
            account.setEmail(signupRequest.getEmail());
            account.setName(signupRequest.getName());
            account.setRoles(Set.of(roleService.getRoleByValue(RoleEnum.ROLE_USER)));
            account.setDisabled(false);
            account.setLocked(false);
            account.setAccountExpiration(OffsetDateTime.now().plusYears(3));
            account.setPasswordExpiration(OffsetDateTime.now().plusYears(1));
            accountService.addAccount(account);

            SignupResponse response = new SignupResponse();
            response.setMessage("Signup Successful");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<AccountDTO>> patchAccount(@PathVariable("username") String username, @RequestBody AccountDTO accountRequest) {
        try {
            logger.info("AuthenticationController.patchAccount({})", username);
            Account account = accountService.updateAccount(username, accountMapper.mapAuthInfo(accountRequest), true);
            AccountDTO accountDTO = accountMapper.mapAuthInfo(account);
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
}
