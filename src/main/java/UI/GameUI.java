package main.java.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.logic.Controller;

/**
 * Created by alexh on 12/19/2016.
 */
public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info;
    private volatile int[][] solnBoard;
    private Controller control;
    private GridPane chat = new GridPane();
    private VBox thisPlayer = new VBox(20);
    private VBox thatPlayer = new VBox(20);
    private HBox sendChat = new HBox(20);

    public GameUI() {
        this("Player 1", Color.RED, new Controller());
    }
    public GameUI(String playerName, Color playerColor, Controller control) {
        this.control = control;
        GridPane squares = new GridPane();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Rectangle box = new Rectangle(255, 255, Color.rgb(0, 0, 0, 0.0));
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(2.5);
                squares.add(box, c, r);
            }
        }
        info = new InfoMenu(playerName, playerColor);
        StackPane center = new StackPane(squares, board);
        chat.add(thisPlayer, 0, 1);
        chat.add(thatPlayer, 1, 1);
        chat.add(new Text("Chat Log:"), 0, 0, 2, 1);
        sendChat.getChildren().add(new TextField("Hello World!"));
        sendChat.getChildren().add(new Button("Send!"));
        this.setTop(info);
        this.setCenter(center);
        this.setLeft(menu);
        this.setRight(chat);
        this.setBottom(sendChat);
        this.thisPlayerChat("Hello!");
        this.thatPlayerChat("Hi!");
        setAlignment(this.getBottom(),
                Pos.CENTER);
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

    public synchronized int[][] getSolnBoard() {
        return solnBoard;
    }

    public synchronized void setSolnBoard(int[][] solnBoard) {
        this.solnBoard = solnBoard;
    }

    public Controller getControl() {
        return this.control;
    }

    public void thisPlayerChat(String chat) {
        thisPlayer.getChildren().add(new Text(chat));
        thatPlayer.getChildren().add(new Text(""));
    }

    public void thatPlayerChat(String chat) {
        thatPlayer.getChildren().add(new Text(chat));
        thisPlayer.getChildren().add(new Text(""));
    }
}
