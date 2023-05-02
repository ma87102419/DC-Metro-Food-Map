package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Account account = accountService.getAccount(username);
            return new User(
                    account.getUsername(),
                    account.getPassword(),
                    !account.getDisabled(),
                    account.getAccountExpiration() == null || account.getAccountExpiration().isAfter(OffsetDateTime.now()),
                    account.getPasswordExpiration().isAfter(OffsetDateTime.now()),
                    !account.getLocked(),
                    account.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getValue().name()))
                            .collect(Collectors.toList())
            );
        }
        catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
