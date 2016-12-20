package main.java.UI;

import javafx.scene.layout.BorderPane;

/**
 * Created by alexh on 12/19/2016.
 */
public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info = new InfoMenu();
    public GameUI() {
        this.setTop(info);
        this.setCenter(board);
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
