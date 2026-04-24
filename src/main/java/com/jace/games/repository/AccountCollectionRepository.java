package com.jace.games.repository;

import com.jace.games.model.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AccountCollectionRepository {
    private final List<Account> accounts = new ArrayList<>();

    public List<Account> findAllAccounts() {
        return this.accounts;
    }

    public Optional<Account> findAccountByUsername(String username) {
        for (Account account : this.accounts) {
            if (account.username().equals(username)) {
                return Optional.of(account);
            }
        }
        return Optional.empty();
    }
}
