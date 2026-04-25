package com.jace.games.controller;

import com.jace.games.model.Account;
import com.jace.games.repository.AccountCollectionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountCollectionRepository repository;


    @Autowired
    AccountController(AccountCollectionRepository accountCollectionRepository) {
        this.repository = accountCollectionRepository;
    }


    // make a request to find every single account on the server and return their usernames
    @GetMapping("/users")
    public List<String> findAllAccounts() {
        return repository.findAllAccounts();
    }
}
