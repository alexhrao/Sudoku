package main.java.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends StackPane {
    private Notes notes = new Notes();
    private Answer answer = new Answer();
    private Rectangle overlay = new Rectangle(85, 85, Color.rgb(0, 0, 0, 0.0));
    private int row;
    private int col;

    public Square(int row, int col, int ans) {
        this(row, col);
        answer.setValue(ans);
    }

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        overlay.setStroke(Color.BLACK);

        this.getChildren().addAll(answer, notes, overlay);
    }

    public Answer getAnswer() {
        return answer;
    }

    public Notes getNotes() {
        return notes;
    }

    public Rectangle getOverlay() {
        return overlay;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

    public void clear() {
        notes.clear();
        answer.clear();
    }
}
