package main.java.server.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * ButtonMenu is a class that defines the left side of the Sudoku Board game. It consists of the Note/Answer button, the
 * Clear button, the Pause/Play button, the Show Answer button, and the Send Game button. It also has the Numbers used
 * to enter the corresponding answer or note.
 */
public class ButtonMenu extends VBox {
    private Button note = new Button("Note");
    private Button clear = new Button("Clear");
    private Button pause = new Button("Pause");
    private Button hint = new Button("Show Answer");
    private Button send = new Button("Send Game");
    private Numbers numbers = new Numbers();

    /**
     * A convenience constructor that uses a padding of 10 to create a ButtonMenu.
     */
    public ButtonMenu() {
        this(10);
    }

    /**
     * A constructor that creates a ButtonMenu with the given padding. This creates a default button menu, with the
     * Note, Clear, Pause, Show Answer, the Numbers, and the Send Game buttons.
     * @param padding An integer that tells how much padding to give.
     */
    public ButtonMenu(int padding) {
        numbers.setPadding(new Insets(padding));
        note.setPadding(new Insets(padding));
        clear.setPadding(new Insets(padding));
        pause.setPadding(new Insets(padding));
        hint.setPadding(new Insets(padding));
        numbers.setPadding(new Insets(padding));
        send.setPadding(new Insets(padding));
        this.getChildren().addAll(note, clear, pause, hint, numbers, send);
    }

    /**
     * A method that gets the Note button.
     * @return The Note button.
     */
    public Button getNote() {
        return note;
    }

    /**
     * A method that gets the Clear button.
     * @return The Clear button.
     */
    public Button getClear() {
        return clear;
    }

    /**
     * A method that gets the Pause button.
     * @return The Pause button.
     */
    public Button getPause() {
        return pause;
    }

    /**
     * A method that gets the Hint button.
     * @return The Hint button.
     */
    public Button getHint() {
        return hint;
    }

    /**
     * A method that returns all of the Numbers.
     * @return The Numbers buttons.
     */
    public Numbers getNumbers() {
        return numbers;
    }

    /**
     * A method that gets the Send button.
     * @return The Send button.
     */
    public Button getSend() {
        return send;
    }

    /**
     * A convenience method that returns a specific Number.
     * @param num The number to get.
     * @return A specific number button.
     */
    public Button getNumber(int num) {
        return numbers.getNumber(num);
    }

    /**
     * A convenience method that disables all the numbers.
     */
    public void disable() {
        note.setDisable(true);
        clear.setDisable(true);
        hint.setDisable(true);
        numbers.disable();
    }

    /**
     * A convenience method that enables all the numbers.
     */
    public void enable() {
        note.setDisable(false);
        clear.setDisable(false);
        hint.setDisable(false);
        numbers.enable();
    }

    /**
     * Numbers class is a GridPane that has a 3x3 grid of the 9 Sudoku numbers.
     */
    private class Numbers extends GridPane {
        private Button[] buttons = new Button[9];

        /**
         * Creates a new Numbers, which is a GridPane with a 3x3 grid of the numbers 1-9.
         */
        public Numbers() {
            for (int r = 0; r < 3; r ++) {
                for (int c = 0; c < 3; c++) {
                    buttons[c + (r * 3)] = new Button(" " + (1 + c + (r * 3)));
                    this.add(buttons[c + (r * 3)], c, r);
                }
            }
        }

        /**
         * Gets the specified numbered button.
         * @param num The number (from 0 to 8) that you wish to retrieve.
         * @return A button that represents this number.
         */
        public Button getNumber(int num) {
            return buttons[num];
        }

        /**
         * Disables this Number.
         */
        public void disable() {
            for (int i = 0; i < 9; i++) {
                buttons[i].setDisable(true);
            }
        }

        /**
         * Enables this Number.
         */
        public void enable() {
            for (int i = 0; i < 9; i++) {
                buttons[i].setDisable(false);
            }
        }
    }
}
