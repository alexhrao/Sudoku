package main.java.server.ui;

import javafx.scene.layout.GridPane;

public class Board extends GridPane {
    private Square[][] squares = new Square[9][9];

    public Board() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.squares[row][col] = new Square(row, col);
                this.add(this.squares[row][col], col, row);
            }
        }
    }

    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    public void setSquare(int row, int col, Square square) {
        this.squares[row][col] = square;
    }

    public void populate(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.squares[row][col].clear();
                this.squares[row][col].getAnswer().setValue(board[row][col]);
            }
        }
    }

}
