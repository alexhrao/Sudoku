package main.java;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Created by alexh on 12/19/2016.
 */
public class Square extends StackPane {

    private Notes notes = new Notes();
    private Text answer = new Text("");

    public Square() {
        this.getChildren().addAll(notes, answer);
    }

    public Square getSquare() {
        return this;
    }

    public Text show(int note) {
        return notes.show(note);

    }

    public Text hide(int note) {
        return notes.hide(note);
    }

    public Text solve(int ans) {
        this.answer.setVisible(true);
        return this.answer;
    }

    public Text wrong(int ans) {
        this.answer.setVisible(false);
        return this.answer;
    }

}
