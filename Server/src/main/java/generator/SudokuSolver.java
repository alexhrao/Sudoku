package main.java.generator;

/**
 * This class solves a given Sudoku Puzzle, given as either an int array, or an array of strings.
 */
public class SudokuSolver {
    private int[][] board;
    private int[][] soln;

    /**
     * It will solve a board given as an array of strings.
     * @param board The board. It MUST follow standard CSV format, with a trailing comma!
     * @throws IllegalArgumentException If the input does not have valid format, or if the board is unsolvable.
     */
    public SudokuSolver(String[] board) throws IllegalArgumentException {
        parseProblem(board);
        this.soln = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(this.board[r], 0, soln[r], 0, 9);
        }
        if (!solve(0, 0)) {
            throw new IllegalArgumentException("Invalid board given.");
        }
    }

    /**
     * Solves a board given as an int array.
     * @param board The int array.
     * @throws IllegalArgumentException If the input is not valid, or if the board is unsolvable.
     */
    public SudokuSolver(int[][] board) throws IllegalArgumentException {
        this.board = board;
        this.soln = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(board[r], 0, soln[r], 0, 9);
        }
        if (!solve(0, 0)) {
            throw new IllegalArgumentException("Invalid board given.");
        }
    }

    /**
     * Get the board.
     * @return The int array of the board; never null.
     */
    public int[][] getBoard() {
        return this.board;
    }

    /**
     * Get the solution to the board.
     * @return The int array of the solution.
     */
    public int[][] getSoln() {
        return soln;
    }

    private boolean solve(int row, int col) {
        if (row == 9) {
            row = 0;
            if (++col == 9)
                return true;
        }
        if (soln[row][col] != 0)  // skip filled cells
            return solve(row+1, col);

        for (int num = 1; num <= 9; num++) {
            if (this.legal(row, col, num)) {
                soln[row][col] = num;
                if (solve(row+1, col))
                    return true;
            }
        }
        soln[row][col] = 0; // reset on backtrack
        return false;
    }

    private boolean legal(int row, int col, int num) {
        for (int r = 0; r < 9; r++) {
            if (num == soln[r][col]) {
                return false;
            }
        }
        for (int c = 0; c < 9; c++) {
            if (num == soln[row][c]) {
                return false;
            }
        }
        int rOff = (row / 3) * 3;
        int cOff = (col / 3) * 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (num == soln[rOff + r][cOff + c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void parseProblem(String[] input) throws IllegalArgumentException {
        try {
            board = new int[9][9];
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    board[r][c] = Integer.parseInt(input[r].substring(0, 1));
                    input[r] = input[r].substring(2);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}