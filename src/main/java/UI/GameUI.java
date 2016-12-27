package main.java.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.logic.Controller;
import main.java.networking.Chat;
import main.java.networking.SudokuClient;

/**
 * Created by alexh on 12/19/2016.
 */
public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info;
    private volatile int[][] solnBoard;
    private Controller control;
    private Chat chat;
    private Chatter chatter;

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
        chat = new Chat(control);
        chatter = new Chatter();
        chatter.getSender().setOnAction(e -> {
            chat.thisPlayerChat(chatter.getChatter().getText());
            SudokuClient client = new SudokuClient(control.getClientHost(), control.getClientPort(), control.getColor(), chatter.getChatter().getText());
            Thread tClient = new Thread(client);
            tClient.start();
        });
        Rectangle overlay = new Rectangle(chat.getChatWidth(), 770, Color.color(0, 0, 0, 0));
        overlay.setStroke(Color.color(0, 0, 0, 1));
        overlay.setStrokeWidth(2);
        ScrollPane chatScroll = new ScrollPane(chat);
        chatScroll.setMaxWidth(300);
        StackPane chatPane = new StackPane(overlay, chatScroll);
        this.setTop(info);
        this.setCenter(center);
        this.setLeft(menu);
        this.setRight(chatPane);
        this.setBottom(chatter);
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

    public Chat getChat() {
        return chat;
    }

    public Chatter getChatter() {
        return chatter;
    }
}
