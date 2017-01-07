package main.java.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

/**
 * This class represents a single square on the Sudoku Board. It has all UI elements associated with a square, such as
 * an Answer and notes.
 */
public class Square extends StackPane {
    private final Notes notes = new Notes();
    private final Answer answer = new Answer();
    private final Rectangle overlay = new Rectangle(
            Screen.getPrimary().getVisualBounds().getHeight() / 12.2353,
            Screen.getPrimary().getVisualBounds().getHeight() / 12.2353,
            Color.TRANSPARENT);
    private boolean selected = false;
    private final int row;
    private final int col;

    /**
     * Creates a Square that has the specified row and column.
     * @param row The row that this square will reside in.
     * @param col The column that this square will reside in.
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        overlay.setStroke(Color.BLACK);
        this.setMinSize(1 + (Screen.getPrimary().getVisualBounds().getHeight() / 12.2353),
                1 + (Screen.getPrimary().getVisualBounds().getHeight() / 12.2353));
        this.setMaxSize(1 + (Screen.getPrimary().getVisualBounds().getHeight() / 12.2353),
                1 + (Screen.getPrimary().getVisualBounds().getHeight() / 12.2353));
        this.getChildren().addAll(overlay, answer, notes);
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
        this.setColor(Color.color(1, 1, 1, 1));
    }

    private void setColor(Color color) {
        this.overlay.setFill(color);
    }

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    public Color getColor() {
//        return (Color) this.overlay.getFill();
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)

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
