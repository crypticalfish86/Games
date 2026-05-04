package com.jace.games.model.tictactoe;

import com.jace.games.model.Account;

import java.time.LocalDateTime;
import java.util.Optional;

public class TicTacToeGame {
    Account X;
    Account O;
    String gameID;
    Board board;
    boolean xTurn;
    Symbol winner;

    public TicTacToeGame(Account X, Account O) {
        this.X = X;
        this.O = O;
        this.gameID = X.toString() + O.toString() + LocalDateTime.now().toString();
        this.board = new Board();
        this.xTurn = true;
    }

    //getter
    public Account getX() {
        return this.X;
    }

    public Account getO() {
        return this.O;
    }

    public String getGameID() {
        return this.gameID;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean checkIfGameOver() {
        return this.winner != null;
    }
    //others

    public Optional<Symbol> takeTurn(Account account, String coordinate) throws IllegalStateException {
        if (xTurn && O.equals(account) || !xTurn && X.equals(account)) {
            throw new IllegalStateException("Not your turn");
        }

        Symbol symbolToAdd = xTurn ? Symbol.X : Symbol.O;
        this.board.addSymbol(coordinate, symbolToAdd);

        if (board.checkWinner(symbolToAdd)) {
            this.winner = symbolToAdd;
        }

        if (winner != null) {
            return Optional.of(this.winner);
        } else {
            return Optional.empty();
        }
    }
}
