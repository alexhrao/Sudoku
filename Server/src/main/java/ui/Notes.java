package main.java.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/**
 * This class represents the Notes that the user can make inside a single Square. It has a few convenience methods for
 * dealing with this notes.
 */
public class Notes extends GridPane {
    private Text[] notes = new Text[9];
    private boolean[] visibility = new boolean[9];

    /**
     * Creates a new Notes object, using a default padding of 10 for the left and right. This is a convenience method.
     */
    public Notes() {
        this(0, 10, 0, 10);
    }

    /**
     * Creates a new Notes object, using the given padding for all four sides.
     * @param padding An integer that represents the number of pixels to pad with.
     */
    public Notes(int padding) {
        this(padding, padding, padding, padding);
    }

    /**
     * Creates a new Notes object, using the four given padding parameters to situate the Note.
     * @param topPad The top padding.
     * @param rightPad The right padding.
     * @param bottomPad The bottom padding.
     * @param leftPad The left padding.
     */
    public Notes(int topPad, int rightPad, int bottomPad, int leftPad) {
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        this.setPadding(new Insets(topPad, rightPad, bottomPad, leftPad));
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                notes[c + (r * 3)] = new Text(" " + Integer.toString(1 + c + (r * 3)) + " ");
                notes[c + (r * 3)].setFont(new Font(screenHeight / 52));
                this.add(notes[c + (r * 3)], c, r);
                this.visibility[c + (r * 3)] = true;
            }
        }
    }

    /**
     * Shows the given note, an index from 0-8.
     * @param note An integer from 0-8, inclusive.
     */
    public void show(int note) {
        notes[note].setVisible(true);
        visibility[note] = true;
    }

    /**
     * Hides the given note, an index from 0-8.
     * @param note An integer from 0-8, inclusive.
     */
    public void hide(int note) {
        notes[note].setVisible(false);
        visibility[note] = false;
    }

    /**
     * Toggles the given note, an index from 0-8. If the note is currently visible, it is hidden, and vice versa.
     * @param note An integer from 0-8, inclusive.
     */
    public void toggle(int note) {
        if (visibility[note]) {
            hide(note);
        } else {
            show(note);
        }
    }

    /**
     * A convenience method that clears the Notes of all visible notes.
     */
    public void clear() {
        for (int k = 0; k < 9; k++) {
            this.hide(k);
        }
    }

    /**
     * Gets the boolean array of visibilities for each note. Note that the indices 0-8 correlate with the numbers
     * 1-9; that is, the index of 7 gives the visibility for the 8 note.
     * @return A boolean array that has all visibilities.
     */
    public boolean[] getVisibility() {
        return visibility;
    }

    /**
     * Checks equality of two Notes instances; it does this by comparing all visibilities.
     * @param obj The given object, which must be of class Notes; otherwise, and IllegalArgumentException is thrown.
     * @return A boolean that signifies if the two Notes objects are equivalent or not.
     * @throws IllegalArgumentException Thrown if the given object is not of class Notes.
     */
    @Override
    public boolean equals(Object obj) throws IllegalArgumentException {
        if (!(obj instanceof Notes)) {
            throw new IllegalArgumentException("Object not of Notes class");
        }
        for (int i = 0; i < 9; i++) {
            if (visibility[i] != ((Notes) obj).visibility[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int out = 0;
        for (int i = 0; i < 9; i++) {
            if (visibility[i]) {
                out += i * 31;
            }
        }
        return out;
    }
}