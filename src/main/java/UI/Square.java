package main.java.UI;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends StackPane {
    private Notes notes = new Notes();
    private Answer answer = new Answer();
    private Rectangle overlay = new Rectangle(130, 130, Color.rgb(0, 0, 0, 0.0));

    public Square(int ans) {
        this();
        answer.setAnswer(ans);
    }

    public Square() {
        overlay.setStroke(Color.BLACK);
        this.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                overlay.setStroke(Color.RED);
            }
        });
        this.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                overlay.setStroke(Color.BLACK);
            }
        });
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
}
