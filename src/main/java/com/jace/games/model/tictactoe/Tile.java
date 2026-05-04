package com.jace.games.model.tictactoe;

public class Tile {
    private Symbol occupyingSymbol;

    public Tile() {
        this.occupyingSymbol = Symbol.NONE;
    }

    public Symbol getSymbol() {
        return this.occupyingSymbol;
    }


    public void addSymbol(Symbol symbol) throws IllegalStateException {
        if (this.occupyingSymbol.equals(Symbol.NONE)) {
            occupyingSymbol = symbol;
        } else {
            throw new IllegalStateException("Error tile is already filled");
        }
    }
}
