package main.java.server.networking;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.server.logic.Controller;

public class Chat extends GridPane {
    private final int SPACING = 20;
    private Controller control;
    private VBox thisPlayer;
    private VBox thatPlayer;
    private HBox sendChat;
    private TextField chatter;
    private int chatWidth;

    public Chat(Controller control) {
        this(300, control);
    }
    public Chat(int chatWidth, Controller control) {
        thisPlayer = new VBox(SPACING);
        thatPlayer = new VBox(SPACING);
        sendChat = new HBox(SPACING);
        this.chatWidth = chatWidth;
        this.control = control;
        this.setMinWidth(chatWidth);
        Text log = new Text("Chat Log:");
        this.add(log, 0, 0, 2, 1);
        this.add(thisPlayer, 0, 1);
        this.add(thatPlayer, 1, 1);
    }

    public void thisPlayerChat(String msg) {
        Text message = new Text(msg);
        message.setFill(control.getColor());
        thisPlayer.getChildren().add(message);
        thatPlayer.getChildren().add(new Text(""));
    }

    public void thatPlayerChat(String msg, Color color) {
        Text message = new Text(msg);
        message.setFill(color);
        thatPlayer.getChildren().add(message);
        thisPlayer.getChildren().add(new Text(""));
    }

    public void thatPlayerChat(String msg) {
        thatPlayerChat(msg, Color.BLACK);
    }

    public VBox getThisPlayer() {
        return thisPlayer;
    }

    public VBox getThatPlayer() {
        return thatPlayer;
    }

    public HBox getSendChat() {
        return sendChat;
    }

    public int getChatWidth() {
        return this.chatWidth;
    }
}
