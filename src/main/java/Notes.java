package main.java;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


/**
 * Created by alexh on 12/19/2016.
 */
public class Notes extends GridPane {
    private Text[] notes = new Text[9];

    public Notes() {
        for (int i = 0; i < 9; i++) {
            this.notes[i] = new Text(Integer.toString(i));
            this.notes[i].setVisible(false);
            this.add(notes[i],i % 3, (int) Math.floor(i / 3));
        }
    }

    public Text show(int note) {
        notes[note - 1].setVisible(true);
        return notes[note - 1];
    }

    public Text hide(int note) {
        notes[note - 1].setVisible(false);
        return notes[note - 1];
    }
}
