package main.java.server.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 * Board is the class responsible for rendering the actual Sudoku Board. It's a GridPane made up of 81 Squares, and
 * has several convenience methods. The Board is exclusive to the client.
 */
public class Board extends GridPane implements Comparable<Board> {
    private Square[][] squares = new Square[9][9];

    /**
     * This creates an empty board. Use populate to actually fill the squres.
     */
    public Board() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.squares[row][col] = new Square(row, col);
                this.add(this.squares[row][col], col, row);
            }
        }
    }

    /**
     * Gets the specified Square object.
     * @param row The row identifier for this square.
     * @param col The column identifier for this square.
     * @return The specified Square.
     */
    public Square getSquare(int row, int col) {
        return squares[row][col];
    }

    /**
     * Set the specified point to a new Square.
     * @param row The row identifier for this square.
     * @param col The column identifier for this square.
     * @param square The replacement square.
     */
    public void setSquare(int row, int col, Square square) {
        this.squares[row][col] = square;
    }

    /**
     * Populate the board, given an integer board. Integers outside of 1-9 are considered blank spots.
     * @param board A double-layered, 81 element array of integers, 1-9.
     */
    public void populate(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.squares[row][col].clear();
                this.squares[row][col].getAnswer().setValue(board[row][col]);
            }
        }
    }

    /**
     * Compare this board to another. They compare as the number of visible spaces.
     * @param board A Board to be compared to.
     * @return Positive if this board is greater than the given board with regards to the number of visible spaces.
     */
    @Override
    public int compareTo(@NotNull Board board) {
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

    /**
     * Checks equality for two boards. Equality is defined as all squares being equal.
     * @param obj The other board.
     * @return A boolean that is true if the two are equal.
     * @throws IllegalArgumentException If the argument is not a board.
     */
    @Override
    public boolean equals(@NotNull Object obj) throws IllegalArgumentException {
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

    /**
     * Returns the hashcode, a combination of how many visible squares and how many correct squares.
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        int thisVis = 0;
        int thisRight = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (this.squares[r][c].isVisible()) {
                    thisVis++;
                }
                if (this.squares[r][c].getOverlay().getFill() != Color.DARKRED) {
                    thisRight++;
                } else {
                    thisRight--;
                }
            }
        }
        return (thisVis * 11) + (thisRight * 17);
    }

}
