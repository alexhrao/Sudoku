package main.java.UI;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Notes extends GridPane {
    private Text[] notes = new Text[9];

    public Notes() {
        this.setPadding(new Insets(0, 10, 0, 10));
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                notes[c + (r * 3)] = new Text(" " + Integer.toString(1 + c + (r * 3)) + " ");
                notes[c + (r * 3)].setFont(new Font(30));
                this.add(notes[c + (r * 3)], c, r);
            }
        }
    }

    public void show(int note) {
        notes[note - 1].setVisible(true);
    }

    public void hide(int note) {
        notes[note - 1].setVisible(false);
    }
}