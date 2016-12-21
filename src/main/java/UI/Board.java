package main.java.UI;

import javafx.scene.layout.GridPane;

/**
 * Created by alexh on 12/19/2016.
 */
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

    public void populate(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.squares[row][col].clear();
                this.squares[row][col].getAnswer().setValue(board[row][col]);
            }
        }
    }

}
