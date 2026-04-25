package com.jace.games.repository;

import com.jace.games.model.Account;
import com.jace.games.model.Profile;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.*;


//Hashmap in the form of (username, Account)
@Repository
public class AccountCollectionRepository {
    private final Map<String, Account> accounts = new HashMap<String, Account>();

    //Returns all existing usernames (not passwords, NEVER send passwords to client).
    public List<String> findAllAccounts() {
        return new ArrayList<>(accounts.keySet());
    }

    //test data
    @PostConstruct //post construct auto runs the method after dependency injection (good for filling test data)
    private void initializeTestData() {
        this.accounts.put("Jace" ,new Account("Jace", "test1", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.put("Leif", new Account("Leif", "test2", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.put("Zac" ,new Account("Zac", "test3", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.put("Jordan", new Account("Jordan", "test4", new ArrayList<>(), LocalDateTime.now()));
        this.accounts.put("Jade", new Account("Jade", "test5", new ArrayList<>(), LocalDateTime.now()));
    }

    //Data edit/access methods

    //Returns a true or false depending on if the username exists
    public boolean checkIfUsernameExists(String username) {
        return this.accounts.containsKey(username);
    }

    //Check if a password is correct (Not secure, in non-personal project don't do this)
    public Optional<Boolean> checkPasswordValidity(String username, String password) {
        if (!this.accounts.containsKey(username)) {
            return Optional.empty();
        } else if (this.accounts.get(username).password().equals(password)){
            return Optional.of(true);
        } else {
            return Optional.of(false);
        }
    }

    //returns all profiles associated with an account
    public Optional<List<Profile>> getAllProfilesOnAccount(String username) {
        if (!this.accounts.containsKey(username)) {
            return Optional.empty();
        } else {
            return Optional.of(this.accounts.get(username).accountProfiles());
        }
    }

    //returns a specific profile associated with an account
    public Profile getProfileOnAccountByGameName(String username, String profileType) throws IllegalArgumentException, NullPointerException {
        if (!this.accounts.containsKey(username)) {
            throw new IllegalArgumentException("Error 404: Account does not exist");
        }

        Account account = this.accounts.get(username);

        if (account == null) {
            throw new NullPointerException("Error 500: Account does exist but no data is on it");
        }

        List<Profile> accountProfiles = account.accountProfiles();


        for (Profile profile : accountProfiles) {
            if (profile.gameName().toString().equals(profileType)) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Error 404: Profile with this game name does not exist on account");
    }

}
