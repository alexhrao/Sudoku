package main.java.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ButtonMenu extends VBox {
    private Button note = new Button("Note");
    private Button clear = new Button("Clear");
    private Button pause = new Button("Pause");
    private Button hint = new Button("Show Answer");
    private Button send = new Button("Send Game");
    private Numbers numbers = new Numbers();

    public ButtonMenu() {
        numbers.setPadding(new Insets(10));
        this.getChildren().addAll(note, clear, pause, hint, numbers, send);
    }

    public Button getNote() {
        return note;
    }

    public Button getClear() {
        return clear;
    }

    public Button getPause() {
        return pause;
    }

    public Button getHint() {
        return hint;
    }

    public Numbers getNumbers() {
        return numbers;
    }

    public Button getSend() {
        return send;
    }

    public Button getNumber(int num) {
        return numbers.getNumber(num);
    }

    public void disable() {
        note.setDisable(true);
        clear.setDisable(true);
        hint.setDisable(true);
        numbers.disable();
    }

    public void enable() {
        note.setDisable(false);
        clear.setDisable(false);
        hint.setDisable(false);
        numbers.enable();
    }

    private class Numbers extends GridPane {
        private Button[] buttons = new Button[9];
        public Numbers() {
            for (int r = 0; r < 3; r ++) {
                for (int c = 0; c < 3; c++) {
                    buttons[c + (r * 3)] = new Button(" " + (1 + c + (r * 3)));
                    this.add(buttons[c + (r * 3)], c, r);
                }
            }
        }

        public Button getNumber(int num) {
            return buttons[num];
        }

        public void disable() {
            for (int i = 0; i < 9; i++) {
                buttons[i].setDisable(true);
            }
        }

        public void enable() {
            for (int i = 0; i < 9; i++) {
                buttons[i].setDisable(false);
            }
        }
    }
}
