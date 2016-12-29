package main.java.server.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.server.logic.Controller;
import main.java.server.networking.Chat;
import main.java.server.networking.SudokuClient;

public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info;
    private volatile Controller control;
    private Chat chat;
    private Chatter chatter;

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
        Rectangle overlay = new Rectangle(chat.getChatWidth() + 6, 770, Color.color(0, 0, 0, 0));
        overlay.setStroke(Color.color(0, 0, 0, 1));
        overlay.setStrokeWidth(4);
        ScrollPane chatScroll = new ScrollPane(chat);
        chatScroll.setFitToWidth(true);
        chatScroll.setMinWidth(300);
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
        return this.control.getSolnBoard();
    }

    public synchronized void setSolnBoard(int[][] solnBoard) {
        this.control.setSolnBoard(solnBoard);
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
