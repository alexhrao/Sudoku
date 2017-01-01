package main.java.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * This class represents a single square on the Sudoku Board. It has all UI elements associated with a square, such as
 * an Answer and notes.
 */
public class Square extends StackPane {
    private Notes notes = new Notes();
    private Answer answer = new Answer();
    private Rectangle overlay = new Rectangle(85, 85, Color.rgb(0, 0, 0, 0.0));
    private boolean selected = false;
    private int row;
    private int col;

    /**
     * Creates a Square that has the specified row and column.
     * @param row The row that this square will reside in.
     * @param col The column that this square will reside in.
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        overlay.setStroke(Color.BLACK);

        this.getChildren().addAll(answer, notes, overlay);
    }

    /**
     * Gets the Answer contained in this square.
     * @return The Answer object.
     */
    public Answer getAnswer() {
        return answer;
    }

    /**
     * Gets the Notes contained in this square.
     * @return The Notes object.
     */
    public Notes getNotes() {
        return notes;
    }

    /**
     * Gets the overlay for this square.
     * @return The rectangular overlay.
     */
    public Rectangle getOverlay() {
        return overlay;
    }

    /**
     * Gets this row.
     * @return The row of this square.
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets this column.
     * @return the column of this square.
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Gets whether or not this square is selected.
     * @return If this square is selected or not.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Sets the selected status.
     * @param selected The selected state of this square.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * A convenience method that clears the square of Notes and Answer.
     */
    public void clear() {
        notes.clear();
        answer.clear();
    }

    /**
     * Checks equivalency between this Square and another. They are the same if their answer and notes are the same,
     * and their overlays, rows, and columns are also equal.
     * @param obj The Square to compare to.
     * @return Whether or not the two objects are equal.
     * @throws IllegalArgumentException If the given object is not of type Square.
     */
    @Override
    public boolean equals(Object obj) throws IllegalArgumentException {
        Square sq;
        if (!(obj instanceof Square)) {
            throw new IllegalArgumentException("Non-Square Object Given");
        } else {
            sq = (Square) obj;
        }
        boolean isNotes = this.notes.equals(sq.notes);
        boolean isAnswer = this.answer.equals(sq.answer);
        boolean isSquare = this.overlay.equals(sq.overlay) && (this.row == sq.row) && (this.col == sq.col);
        return isNotes && isAnswer && isSquare;
    }
}