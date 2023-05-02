package com.foodie.foodmapapi.mappers;

import com.foodie.foodmapapi.dtos.AccountDTO;
import com.foodie.foodmapapi.models.Account;
import com.foodie.foodmapapi.models.Role;
import com.foodie.foodmapapi.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final RoleService roleService;

    public AccountDTO mapAuthInfo(Account account) {
        AccountDTO dto = mapPrivateInfo(account);
        dto.setRoles(account.getRoles().stream().map(Role::getValue).collect(Collectors.toSet()));
        dto.setLocked(account.getLocked());
        dto.setDisabled(account.getDisabled());
        dto.setAccountExpiration(account.getAccountExpiration());
        return dto;
    }

    public AccountDTO mapPrivateInfo(Account account) {
        AccountDTO dto = mapPublicInfo(account);
        dto.setEmail(account.getEmail());
        dto.setPasswordExpiration(account.getPasswordExpiration());
        return dto;
    }

    public AccountDTO mapPublicInfo(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setUsername(account.getUsername());
        dto.setName(account.getName());
        return dto;
    }

    public Account mapAuthInfo(AccountDTO dto) {
        Account account = mapPrivateInfo(dto);
        if (dto.getRoles() != null)
            account.setRoles(dto.getRoles().stream().map(roleService::getRoleByValue).collect(Collectors.toSet()));
        account.setLocked(dto.getLocked());
        account.setDisabled(dto.getDisabled());
        account.setAccountExpiration(dto.getAccountExpiration());
        account.setPasswordExpiration(dto.getPasswordExpiration());
        return account;
    }

    public Account mapPrivateInfo(AccountDTO dto) {
        Account account = new Account();
        account.setUsername(dto.getUsername());
        account.setPassword(dto.getPassword());
        account.setEmail(dto.getEmail());
        account.setName(dto.getName());
        return account;
    }
}
