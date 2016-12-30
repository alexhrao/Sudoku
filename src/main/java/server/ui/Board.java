package main.java.server.ui;

import javafx.scene.layout.GridPane;

public class Board extends GridPane implements Comparable<Board> {
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

    public int compareTo(Board board) {
        int thisVis = 0;
        int thatVis = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (this.squares[r][c].isVisible()) {
                    thisVis++;
                }
                if (board.squares[r][c].isVisible()) {
                    thatVis++;
                }
            }
        }
        return thisVis - thatVis;
    }

    @Override
    public boolean equals(Object obj) throws IllegalArgumentException {
        if (!(obj instanceof Board)) {
            throw new IllegalArgumentException("Object not of class Board");
        }
        Board board = (Board) obj;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (!(board.squares[r][c].equals(this.squares[r][c]))) {
                    return false;
                }
            }
        }
        return true;
    }

}
