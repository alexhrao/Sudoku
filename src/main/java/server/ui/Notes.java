package main.java.server.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Notes extends GridPane {
    private Text[] notes = new Text[9];
    private boolean[] visibility = new boolean[9];

    public Notes() {
        this.setPadding(new Insets(0, 10, 0, 10));
        for (int c = 0; c < 3; c++) {
            for (int r = 0; r < 3; r++) {
                notes[c + (r * 3)] = new Text(" " + Integer.toString(1 + c + (r * 3)) + " ");
                notes[c + (r * 3)].setFont(new Font(20));
                this.add(notes[c + (r * 3)], c, r);
                this.visibility[c + (r * 3)] = true;
            }
        }
    }

    public void show(int note) {
        notes[note].setVisible(true);
        visibility[note] = true;
    }

    public void hide(int note) {
        notes[note].setVisible(false);
        visibility[note] = false;
    }

    public void toggle(int note) {
        if (visibility[note]) {
            hide(note);
        } else {
            show(note);
        }
    }
    public void clear() {
        for (int k = 0; k < 9; k++) {
            this.hide(k);
        }
    }
    public boolean[] getVisibility() {
        return visibility;
    }
}
