package main.java.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 * Created by alexh on 12/19/2016.
 */
public class ButtonMenu extends VBox {
    private Button note = new Button("Note");
    private Button clear = new Button("Clear");
    private Button pause = new Button("Pause");
    private Button hint = new Button("Hint");
    private Numbers numbers = new Numbers();

    public ButtonMenu() {
        numbers.setPadding(new Insets(10));
        this.getChildren().addAll(note, clear, pause, hint, numbers);
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

    public Button hint() {
        return hint;
    }

    public Numbers getNumbers() {
        return numbers;
    }

    private class Numbers extends GridPane {
        public Numbers() {
            for (int r = 0; r < 3; r ++) {
                for (int c = 0; c < 3; c++) {
                    this.add(new Button(" " + (1 + c + (r * 3))), c, r);
                }
            }
        }
    }
}
