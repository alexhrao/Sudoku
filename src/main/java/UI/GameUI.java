package main.java.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by alexh on 12/19/2016.
 */
public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info = new InfoMenu();
    public GameUI() {
        GridPane squares = new GridPane();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Rectangle box = new Rectangle(255, 255, Color.rgb(0, 0, 0, 0.0));
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(2.5);
                squares.add(box, c, r);
            }
        }
        StackPane center = new StackPane(squares, board);
        this.setTop(info);
        this.setCenter(center);
        this.setLeft(menu);
    }

    public Board getBoard() {
        return board;
    }

    public ButtonMenu getMenu() {
        return menu;
    }

    public InfoMenu getInfo() {
        return info;
    }
}
