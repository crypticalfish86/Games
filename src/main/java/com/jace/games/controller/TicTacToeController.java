package com.jace.games.controller;

import com.jace.games.model.Account;
import com.jace.games.model.tictactoe.TicTacToeGame;
import com.jace.games.repository.AccountCollectionRepository;
import com.jace.games.repository.TicTacToeCollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RestController
@RequestMapping("/TicTacToe")
public class TicTacToeController {

    private final TicTacToeCollectionRepository ticTacToeRepository;
    private final AccountCollectionRepository accountRepository; //The repository containing all the accounts
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(); //The read/write lock we're using to ensure concurrency


    @Autowired
    public TicTacToeController(TicTacToeCollectionRepository ticTacToeRepo, AccountCollectionRepository accountRepo) {
        this.ticTacToeRepository = ticTacToeRepo;
        this.accountRepository = accountRepo;
    }

    //GET

    //get all past games
    //get all active games
    @GetMapping("/games/{accountUsername}")
    public ResponseEntity<List<TicTacToeGame>> getAccountGames(
            @PathVariable(value = "accountUsername") String username,
            @RequestParam(value = "activeGame") String activeGames
    ) {
        Account account = accountRepository.getAccount(username);
        List<TicTacToeGame> allGames =
                activeGames.equals("true") ?
                        ticTacToeRepository.getActiveGamesOfAccount(account) :
                        ticTacToeRepository.getPastGamesOfAccount(account);

        if (allGames.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(allGames);
    }

    //get specific game
    @GetMapping("/games/{accountUsername}/{gameID}")
    public ResponseEntity<TicTacToeGame> getSpecificGame(
            @PathVariable(value = "accountUsername") String username,
            @PathVariable(value = "gameID") String gameID
    ) {
        try {
            Account account = accountRepository.getAccount(username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        try {
            TicTacToeGame game = ticTacToeRepository.getSpecificGame(gameID);
            return ResponseEntity.status(HttpStatus.OK).body(game);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //POST

    //start new game
    @PostMapping("/games")
    public ResponseEntity<TicTacToeGame> newGame(
            @RequestParam(value = "account1") String username1,
            @RequestParam(value = "account2") String username2
    ) {
        Account account1 = accountRepository.getAccount(username1);
        Account account2 = accountRepository.getAccount(username2);
        TicTacToeGame newGame = ticTacToeRepository.startNewGameAndReturnID(account1, account2);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGame);
    }

    //PUT

    //take instruction
    @PutMapping("/games")
    public ResponseEntity<TicTacToeGame> inputTurnInstruction(
            @RequestParam(value = "gameID") String gameID,
            @RequestParam(value = "coordinate") String instruction,
            @RequestBody Account account
    ) {
        TicTacToeGame editedGame = ticTacToeRepository.inputNewInstruction(account, gameID, instruction);
        return ResponseEntity.status(HttpStatus.OK).body(editedGame);
    }


    //DELETE

    //delete game
    @DeleteMapping("/games")
    public ResponseEntity<TicTacToeGame> deleteActiveGame(
            @RequestParam(value = "gameID") String gameID
    ) {
        TicTacToeGame deletedGame = ticTacToeRepository.deleteActiveGame(gameID);
        return ResponseEntity.status(HttpStatus.OK).body(deletedGame);
    }
}
