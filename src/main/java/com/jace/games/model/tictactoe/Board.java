package com.jace.games.model.tictactoe;

public class Board {
    Tile[][] board;

    public Board() {
        this.board = new Tile[3][3];
        for (Tile[] row : this.board) {
            for(Tile tile : row) {
                tile = new Tile();
            }
        }
    }

    //getters
    public Tile[][] getBoardTiles() {
        return this.board;
    }

    public Tile[][] addSymbol(String coordinate, Symbol symbolType) throws IllegalArgumentException {
        coordinateToTile(coordinate).addSymbol(symbolType);
        return this.board;
    }
        private Tile coordinateToTile(String coordinate) {
            return switch(coordinate.toLowerCase()) {
                case "top-left" ->  this.board[2][0];
                case "top-middle" -> this.board[2][1];
                case "top-right" -> this.board[2][2];
                case "middle-left" -> this.board[1][0];
                case "middle-middle" -> this.board[1][1];
                case "middle-right" -> this.board[1][3];
                case "bottom-left" -> this.board[0][0];
                case "bottom-middle" -> this.board[0][1];
                case "bottom-right" -> this.board[0][2];
                default -> throw new IllegalArgumentException("Tile does not exist");
            };
        }

    public boolean checkWinner(Symbol symbol) {
        return checkHorizontally(symbol) || checkVertically(symbol) || checkDiagonally(symbol);
    }
        private boolean checkHorizontally(Symbol symbol) {
            for (Tile[] row : this.board) {

                int symbolCount = 0;
                for (Tile tile : row) {
                    if (tile.getSymbol().equals(symbol)) {
                        symbolCount++;
                    }
                }

                if (symbolCount > 2) {
                    return true;
                }
            }
            return false;
        }
        private boolean checkVertically(Symbol symbol) {
            for (int x = 0; x < this.board[0].length; x++) {
                int symbolCount = 0;
                for (int y = 0; y < this.board.length; y++) {
                    if (this.board[y][x].equals(symbol)) {
                        symbolCount++;
                    }
                }

                if (symbolCount > 2) {
                    return true;
                }
            }
            return false;
        }
        private boolean checkDiagonally(Symbol symbol) {
            return (board[0][0].getSymbol().equals(symbol) && board[1][1].getSymbol().equals(symbol) && board[2][2].getSymbol().equals(symbol))
                    ||
                    (board[2][0].getSymbol().equals(symbol) && board[1][1].getSymbol().equals(symbol) && board[0][2].getSymbol().equals(symbol));
        }
}
