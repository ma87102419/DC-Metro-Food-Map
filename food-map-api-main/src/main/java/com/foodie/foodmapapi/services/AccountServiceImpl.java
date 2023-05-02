package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.configs.ErrorMessagesProperties;
import com.foodie.foodmapapi.exceptions.InvalidParameterException;
import com.foodie.foodmapapi.exceptions.NotFoundException;
import com.foodie.foodmapapi.models.Account;
import com.foodie.foodmapapi.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    public static final String USERNAME_REGEX = "^[\\w]*[a-zA-Z0-9][\\w]*$";
    public static final int USERNAME_MIN_LENGTH = 4;
    public static final int USERNAME_MAX_LENGTH = 16;
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ErrorMessagesProperties errorMessages;



    @Override
    public Account getAccount(String username) {
        return accountRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new NotFoundException(errorMessages.getNotFoundByUsername() + username));
    }

    @Override
    public Account addAccount(Account account) {
        // Validate Username
        if (!isValidUsername(account.getUsername())) {
            throw new InvalidParameterException(errorMessages.getInvalidUsername());
        }
        accountRepository.findByUsernameIgnoreCase(account.getUsername()).ifPresent(a -> {
            throw new InvalidParameterException(errorMessages.getTakenUsername());
        });

        // Validate Email
        if (isInvalidEmail(account.getEmail())) {
            throw new InvalidParameterException(errorMessages.getInvalidEmail());
        }
        accountRepository.findByEmailIgnoreCase(account.getEmail()).ifPresent(a -> {
            throw new InvalidParameterException(errorMessages.getTakenEmail());
        });

        // Validate Password
        if (account.getPassword() == null || account.getPassword().isEmpty()) {
            throw new InvalidParameterException(errorMessages.getMissingPassword());
        }

        // Encode Password
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Perform Addition
        return accountRepository.save(account);
    }

    @Override
    public Account removeAccount(String username) {
        // Validate Parameters
        Account account = this.getAccount(username);

        // Perform Deletion
        accountRepository.deleteById(username);
        return account;
    }

    @Override
    public Account updateAccount(String username, Account account, boolean updateAuthDetails) {
        // Validate Username
        Account oldAccount = this.getAccount(username);

        // Validate Email
        if (account.getEmail() != null) {
            if (isInvalidEmail(account.getEmail())) {
                throw new InvalidParameterException(errorMessages.getInvalidEmail());
            }
            if (!oldAccount.getEmail().equals(account.getEmail())) {
                accountRepository.findByEmailIgnoreCase(account.getEmail()).ifPresent(a -> {
                    throw new InvalidParameterException(errorMessages.getTakenEmail());
                });
                oldAccount.setEmail(account.getEmail());
            }
        }

        // Validate and Encode Password, Also update password expiration
        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            oldAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            oldAccount.setPasswordExpiration(OffsetDateTime.now()
                    .withOffsetSameInstant(oldAccount.getPasswordExpiration().getOffset())
                    .truncatedTo(ChronoUnit.DAYS)
                    .plusDays(1)
                    .plusYears(1));
        }

        // Update Non-Null Values (except settings and group)
        oldAccount.setName(account.getName() != null ? account.getName() : oldAccount.getName());
        if (updateAuthDetails) {
            oldAccount.setRoles(!account.getRoles().isEmpty() ? account.getRoles() : oldAccount.getRoles());
            oldAccount.setLocked(account.getLocked() != null ? account.getLocked() : oldAccount.getLocked());
            oldAccount.setDisabled(account.getDisabled() != null ? account.getDisabled() : oldAccount.getDisabled());
            oldAccount.setAccountExpiration(account.getAccountExpiration() != null ? account.getAccountExpiration() : oldAccount.getAccountExpiration());
            oldAccount.setPasswordExpiration(account.getPasswordExpiration() != null ? account.getPasswordExpiration() : oldAccount.getPasswordExpiration());
        }

        // Perform Update
        return accountRepository.save(oldAccount);
    }

    @Override
    public boolean isValidUsername(String username) {
        return username != null && username.matches(USERNAME_REGEX) && username.length() >= USERNAME_MIN_LENGTH && username.length() <= USERNAME_MAX_LENGTH;
    }

    private boolean isInvalidEmail(String email) {
        return email == null || !email.matches(EMAIL_REGEX);
    }
}
