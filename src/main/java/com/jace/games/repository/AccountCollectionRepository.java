package com.jace.games.repository;

import com.jace.games.model.Account;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AccountCollectionRepository {
    private final List<Account> accounts = new ArrayList<>();

    //Returns all existing usernames (not passwords, NEVER send passwords to client).
    public List<String> findAllAccounts() {
        return this.accounts.stream()
                .map(account -> account.username())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //test data
    @PostConstruct //post construct auto runs the method after dependency injection (good for filling test data)
    private void initializeTestData() {
        this.accounts.add(new Account("Jace", "test1", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.add(new Account("Leif", "test2", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.add(new Account("Zac", "test3", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.add(new Account("Jordan", "test4", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.add(new Account("Jade", "test5", new ArrayList<>(), LocalDateTime.now()));
    }

    public Optional<String> findAccountByUsername(String username) {
        for (Account account : this.accounts) {
            if (account.username().equals(username)) {
                return Optional.of(account.username());
            }
        }
        return Optional.empty();
    }
}
