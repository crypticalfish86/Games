package com.jace.games.controller;

import com.jace.games.model.Account;
import com.jace.games.model.Profile;
import com.jace.games.repository.AccountCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountCollectionRepository accountRepository; //The repository containing all the accounts
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); //The read/write lock we're using to ensure concurrency


    @Autowired
    AccountController(AccountCollectionRepository accountCollectionRepository) {
        this.accountRepository = accountCollectionRepository;
    }


    //GET

    /**
     * Make a request to find every single account on the server and return their usernames.
     * @return A response entity detailing either a list of all usernames or an error status.
     */
    @GetMapping(
            value = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<String>> findAllAccounts() {

        lock.readLock().lock();
        List<String> allAccounts = accountRepository.findAllAccounts();
        lock.readLock().unlock();

        if (allAccounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(allAccounts);
    }

    /**
     * Check if you have valid login credentials.
     * @param username the username of the account you want to check exists.
     * @param password the password of the account you want to check exists.
     * @return A response entity containing the valid account or an error status.
     */
    @GetMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Account> checkAccountValidity(
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
            ) {
        lock.readLock().lock();
        boolean validDetails = accountRepository.checkAccountValidity(username, password);
        lock.readLock().unlock();
        if (!validDetails) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
    }

    /**
     * Make a request to find all profiles of a single account on the server.
     * @param username the username of the account you want to get profiles from.
     * @return A response entity either containing a list of all profiles on an account, or an error status.
     */
    @GetMapping(
            value = "/profiles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Profile>> getAllProfilesOnAccount(
            @RequestParam(value = "username") String username
    ) {
        lock.readLock().lock();
        Optional<List<Profile>> allProfiles = accountRepository.getAllProfilesOnAccount(username);
        lock.readLock().unlock();

        if (allProfiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(allProfiles.get());
        }
    }

    /**
     * Make a request to find a specific game profile of a single account on the server.
     * @param gameName The name of the game you're trying to request a profile from.
     * @param username the username of the account you want to get the game profile of.
     * @return A response entity either containing the requested profile or an error status.
     */
    @GetMapping(
            value = "/profiles/{gameName}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Profile> getSpecificProfileOnAccount(
            @PathVariable(value = "gameName") String gameName,
            @RequestParam(value="username") String username
    ) {
        lock.readLock().lock();
        try {
            Profile profile = accountRepository.getProfileOnAccountByGameName(username, gameName);
            lock.readLock().unlock();
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        } catch (Exception e) {
            lock.readLock().unlock();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    //POST

    /**
     * Post a new account to the server.
     * @param account The account with a username and password to add to the server.
     * @return Response entity containing the successfully added account or an error status if it failed.
     */
    @PostMapping(value = "/users")
    public ResponseEntity<Account> addAccount(
            @RequestBody Account account
    ) {
        lock.writeLock().lock();
        try {
            Account addedAccount = accountRepository.addNewAccount(account.username(), account.password());
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.CREATED).body(addedAccount);
        } catch (Exception e) {
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Add a new profile to the account.
     * @param username The username of the account we're adding the profile to.
     * @param gameName The game name of the profile we're adding.
     * @return The account with the newly added profile.
     */
    @PostMapping(value = "profiles/{gameName}")
    public ResponseEntity<Account> addProfileByGameName(
            @PathVariable(value = "gameName") String gameName,
            @RequestParam(value="username") String username
    ) {
        lock.writeLock().lock();
        //Try adding the profile
        try {
            Account modifiedAccount = accountRepository.addNewProfileOnAccount(username, gameName);
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.CREATED).body(modifiedAccount);
        } catch (Exception e) {
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    //PUT

    /**
     * Either increment the wins or losses on an account on a specific game.
     * @param username The username of the account you're incrementing a win/loss on.
     * @param gameName The name of the game you're incrementing a win/loss on.
     * @param winFlag A flag attached as a query to the url, it's either "true" for wins or "false" for losses.
     * @return A response entity containing the now modified account or an error status.
     */
    @PutMapping(value = {"profiles/{gameName}/increment"})
    public ResponseEntity<Account> incrementWinOrLoss(
            @PathVariable(value = "gameName", required = true) String gameName,
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "winFlag", required = true) String winFlag
    ) {
        lock.writeLock().lock();
        //increment the win or loss depending on the winFlag query part of the domain
        try {
            Account modifiedAccount;
            if (winFlag.equals("true")) {
                modifiedAccount = accountRepository.incrementWinsOnProfile(username, gameName);
                lock.writeLock().unlock();
                return ResponseEntity.status(HttpStatus.OK).body(modifiedAccount);
            } else if (winFlag.equals("false")) {
                modifiedAccount = accountRepository.incrementLossesOnProfile(username, gameName);
                lock.writeLock().unlock();
                return ResponseEntity.status(HttpStatus.OK).body(modifiedAccount);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    //DELETE

    /**
     * Delete an account on the server.
     * @param account The account you want to delete.
     * @return A response entity containing the now deleted account or an error status.
     */
    @DeleteMapping(value = "")
    public ResponseEntity<Account> deleteAccount(@RequestBody Account account) {
        lock.writeLock().lock();
        //Check if the incoming credentials from the client match an account in the servers records
        if (!accountRepository.checkAccountValidity(account.username(), account.password())) {
            lock.writeLock().unlock();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        accountRepository.deleteAccount(account.username());
        lock.writeLock().unlock();
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}
