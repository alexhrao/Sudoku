package main.java.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The Answer Class represents an Answer in the Sudoku game. This is a number that appears to completely fill
 * the given square. It can have many colors, though three are reserved: Black is a starting answer, Green is a given
 * answer, and Dark Red is an incorrect answer. Any other color represents the player who correctly answered that square.
 */
public class Answer extends Text implements Comparable<Answer> {
    private boolean isVisible = false;
    private int value = 0;

    /**
     * Creates a new answer, with default font size of 50 and is not visible.
     */
    public Answer() {
        this.clear();
        this.setFont(new Font(50));
        this.setFill(Color.BLACK);
        this.setVisible(false);
    }

    /**
     * Sets the value of the answer. If it is not between 1 and 9, the value is set to 0 and the Answer is not visible.
     * @param ans The integer that represents the answer.
     */
    public void setValue(int ans) {
        if (ans > 9 || ans < 1) {
            this.clear();
        } else {
            this.setText(Integer.toString(ans));
            this.setVisible(true);
            isVisible = true;
            value = ans;
        }
    }

    /**
     * Gets the value of this Answer, as an integer.
     * @return The integer value.
     */
    public int getValue() {
        return value;
    }

    /**
     * A Convenience method that will set the text to nothing, make it invisible, and reset the value to 0.
     */
    public void clear() {
        this.setText("");
        this.setVisible(false);
        isVisible = false;
        value = 0;
    }

    /**
     * A method that tells whether or not this Answer is visible.
     * @return A boolean that tells whether or not the Answer is visible.
     */
    public boolean getVisible() {
        return isVisible;
    }

    /**
     * Compares this Answer to a given Answer.
     * @param answer The answer object we are comparing to.
     * @return An integer that is positive if this square has a greater value than the given square.
     */
    @Override
    public int compareTo(Answer answer) {
        return answer.getValue() - this.getValue();
    }

    /**
     * Two Answers are equivalent if their values are the same and their fills are the same. It will throw an
     * IllegalArgumentException if the given Object is not of class Answer.
     * @param object The object we are comparing to.
     * @return A boolean defining whether or not the two Answers are equivalent.
     */
    @Override
    public boolean equals(Object object) throws IllegalArgumentException {
        if (object == null || !(object instanceof Answer)) {
            throw new IllegalArgumentException("Valid variable of class Answer not given!");
        } else {
            return (((Answer) object).getValue() == this.getValue()) && ((Answer) object).getFill().equals(this.getFill());
        }
    }

    /**
     * Returns the hash of this answer, defined as a combination of the answer and it's color components.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return (31 * this.value) + (int) (((Color) this.getFill()).getRed() * 23) +
                (int) (((Color) this.getFill()).getGreen() * 7) + (int) (((Color) this.getFill()).getBlue() * 19);
    }
}
