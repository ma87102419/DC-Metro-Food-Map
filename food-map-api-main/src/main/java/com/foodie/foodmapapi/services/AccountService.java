package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.models.Account;

public interface AccountService {

    /**
     * Retrieve Account with username from database
     * @param username
     * @return
     */
    Account getAccount(String username);

    /**
     * Create Account from account and add to database
     * @param account
     * @return
     */
    Account addAccount(Account account);

    /**
     * Remove Account with username from database
     * @param username
     * @return
     */
    Account removeAccount(String username);

    /**
     * Update Account with accountID in database with account
     * @param username
     * @param account
     * @return
     */
    Account updateAccount(String username, Account account, boolean updateAuthDetails);

    /**
     * Check if username is a valid Username
     * @param username
     * @return
     */
    boolean isValidUsername(String username);
}
