package com.jace.games.repository;

import com.jace.games.model.Account;
import com.jace.games.model.Profile;
import com.jace.games.model.ProfileType;
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
    /*
    @PostConstruct //post construct auto runs the method after dependency injection (good for filling test data)
    private void initializeTestData() {
        this.accounts.put("Jace" ,new Account("Jace", "test1", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        this.accounts.put("Leif", new Account("Leif", "test2", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        this.accounts.put("Zac" ,new Account("Zac", "test3", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        this.accounts.put("Jordan", new Account("Jordan", "test4", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
        this.accounts.put("Jade", new Account("Jade", "test5", new ArrayList<>(), LocalDateTime.now(), LocalDateTime.now()));
    }
    */
    //Data edit/access methods

    //GET
    //Returns a true or false depending on if the username exists
    public boolean checkIfUsernameExists(String username) {
        return this.accounts.containsKey(username);
    }

    //get account
    public Account getAccount(String username) {
        if (this.accounts.containsKey(username)) {
            return accounts.get(username);
        } else {
            throw new NullPointerException("No account with this username");
        }
    }

    //Check if account details are correct password (Not secure, in non-personal project don't do this)
    public boolean checkAccountValidity(Account accountToCheck) {
        if (!this.accounts.containsKey(accountToCheck.username())) {
            return false;
        } else {
            return this.accounts.get(accountToCheck.username()).password().equals(accountToCheck.password());
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
    public Profile getProfileOnAccountByGameName(String username, String gameName) throws IllegalArgumentException, NullPointerException {
        if (!this.accounts.containsKey(username)) {
            throw new IllegalArgumentException("Error 404: Account does not exist");
        }

        Account account = this.accounts.get(username);

        if (account == null) {
            throw new NullPointerException("Error 500: Account does exist but no data is on it");
        }

        List<Profile> accountProfiles = account.accountProfiles();


        for (Profile profile : accountProfiles) {
            if (profile.gameName().toString().equals(gameName)) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Error 404: Profile with this game name does not exist on account");
    }

    //POST

    //Create a new account
    public Account addNewAccount(String username, String password) throws IllegalArgumentException {
        if (accounts.containsKey(username)) {
            throw new IllegalArgumentException("Error 400: Account with this username already exists");
        }

        ArrayList<Account> allAccounts = new ArrayList<>(accounts.values());
        for (Account account : allAccounts) {
            if (account.password().equals(password)) {
                throw new IllegalArgumentException("Error 400: That password is already taken");
            }
        }

        Account newAccount = new Account(username, password, new ArrayList<Profile>(), LocalDateTime.now(), LocalDateTime.now());
        accounts.put(username, newAccount);
        return newAccount;
    }


    //create a new profile on an account
    public Account addNewProfileOnAccount(String username, String gameName) throws IllegalArgumentException, NullPointerException {

        Account account = accounts.get(username);
        if (account == null) {
            throw new IllegalArgumentException("Error 400: That account does not exist");
        }

        if (!checkGameProfileIsValidType(gameName)) {
            throw new IllegalArgumentException("Error 400: A game with that name does not exist");
        }

        List<Profile> existingProfiles = account.accountProfiles();
        for (Profile profile : existingProfiles) {
            if (profile.gameName().toString().equals(gameName)) {
                throw new IllegalArgumentException("Error 400: Profile for that game already exists");
            }
        }

        Profile profileToAdd = new Profile(username, stringToProfileType(gameName), 0, 0, LocalDateTime.now());
        account.accountProfiles().add(profileToAdd);
        return account;
    }
        //HELPER: Check if the gameName represents a valid profile type
        private boolean checkGameProfileIsValidType(String gameName) {

            ProfileType[] possibleTypes = ProfileType.values();
            for (ProfileType profileType : possibleTypes) {
                if (profileType.toString().equals(gameName.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
        //HELPER: Convert gameName string to actual profile type enum
        private ProfileType stringToProfileType(String gameName) {

            ProfileType[] possibleTypes = ProfileType.values();
            for (ProfileType profileType : possibleTypes) {
                if (profileType.toString().equals(gameName.toUpperCase())) {
                    return profileType;
                }
            }
            throw new NullPointerException("Error 400: A game with that name does not exist");
        }


    //PUT

    //increment the wins on a specific account profile
    public Account incrementWinsOnProfile(String username, String gameName) {

        Account account = accounts.get(username);
        if (account == null) {
            throw new IllegalArgumentException("Error 400: An account with that username doesn't exist");
        }

        for (int i = 0; i < account.accountProfiles().size(); i++) {
            Profile profile = account.accountProfiles().get(i);

            if (profile.gameName().toString().equals(gameName)) {
                Profile updatedProfile = profile.incrementWins();
                account.accountProfiles().set(i, updatedProfile);
                return account;
            }
        }

        throw new IllegalArgumentException("Error 404: A profile with that gameName was not found on this account");
    }

    //increment losses on a specific account profile
    public Account incrementLossesOnProfile(String username, String gameName) {

        Account account = accounts.get(username);
        if (account == null) {
            throw new IllegalArgumentException("Error 400: An account with that username doesn't exist");
        }


        for (int i = 0; i < account.accountProfiles().size(); i++) {
            Profile profile = account.accountProfiles().get(i);

            if (profile.gameName().toString().equals(gameName)) {
                Profile updatedProfile = profile.incrementLosses();
                account.accountProfiles().set(i, updatedProfile);
                return account;
            }
        }

        throw new IllegalArgumentException("Error 404: A profile with that gameName was not found on this account");
    }

    //DELETE

    public void deleteAccount(String username) throws IllegalArgumentException {
        if (accounts.containsKey(username)) {
            accounts.remove(username);
        } else {
            throw new IllegalArgumentException("Error 404: An account with that name doesn't exist");
        }
    }
}
