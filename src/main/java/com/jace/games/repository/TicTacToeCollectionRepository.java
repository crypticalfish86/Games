package com.jace.games.repository;

import com.jace.games.model.Account;
import com.jace.games.model.tictactoe.TicTacToeGame;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicTacToeCollectionRepository {

    private final List<TicTacToeGame> ticTacToeGames = new ArrayList<TicTacToeGame>();

    //GET

    //get all active games associated with account
    public List<TicTacToeGame> getActiveGamesOfAccount(Account account) {
        List<TicTacToeGame> allGames = new ArrayList<>();
        for (TicTacToeGame game : ticTacToeGames) {
            if (game.getX().equals(account) || game.getO().equals(account)) {
                if (!game.checkIfGameOver()) {
                    allGames.add(game);
                }
            }
        }
        return allGames;
    }

    //get all games past associated with account
    public List<TicTacToeGame> getPastGamesOfAccount(Account account) {
        List<TicTacToeGame> allGames = new ArrayList<>();
        for (TicTacToeGame game : ticTacToeGames) {
            if (game.getX().equals(account) || game.getO().equals(account)) {
                if (game.checkIfGameOver()) {
                    allGames.add(game);
                }
            }
        }
        return allGames;
    }


    //get specific game associated with account
    public TicTacToeGame getSpecificGame(String gameID) throws NullPointerException {
        for (TicTacToeGame game :ticTacToeGames) {
            if (game.getGameID().equals(gameID)) {
                return game;
            }
        }
        throw new NullPointerException("Game not found");
    }


    //POST
    public String startNewGameAndReturnID(Account X, Account O) {
        TicTacToeGame newGame = new TicTacToeGame(X, O);
        this.ticTacToeGames.add(newGame);
        return newGame.getGameID();
    }

    //PUT
    public TicTacToeGame inputNewInstruction(Account account, String gameID, String coordinate) {
        TicTacToeGame relevantGame = null;
        for (TicTacToeGame game : this.ticTacToeGames) {
            if (game.getGameID().equals(gameID)) {
                relevantGame = game;
                break;
            }
        }

        if (relevantGame == null) {
            throw new NullPointerException("Error, game not found");
        }

        if (relevantGame.checkIfGameOver()) {
            throw new IllegalArgumentException("Error, game is already over");
        }

        relevantGame.takeTurn(account, coordinate);
        return relevantGame;
    }

    //DELETE
    public void deleteActiveGame(String gameID) {
        for (int i = 0; i < this.ticTacToeGames.size(); i++) {
            if (ticTacToeGames.get(i).getGameID().equals(gameID)) {
                ticTacToeGames.remove(i);
                return;
            }
        }
        throw new NullPointerException("Error, game not found");
    }
}
