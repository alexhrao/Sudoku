package main.java.networking;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.logic.Controller;

public class Chat extends GridPane {
    private final int SPACING = 20;
    private Controller control;
    private VBox thisPlayer;
    private VBox thatPlayer;
    private HBox sendChat;
    private TextField chatter;
    private int chatWidth;

    /**
     *
     * @param control The controller.
     */
    public Chat(Controller control) {
        this(300, control);
    }

    /**
     *
     * @param chatWidth A preferred int width for the chat box.
     * @param control The controller.
     */
    public Chat(int chatWidth, Controller control) {
        thisPlayer = new VBox(SPACING);
        thisPlayer.setMinWidth(chatWidth / 2);
        thatPlayer = new VBox(SPACING);
        thatPlayer.setMinWidth(chatWidth / 2);
        sendChat = new HBox(SPACING);
        this.chatWidth = chatWidth;
        this.control = control;
        this.setMinWidth(chatWidth);
        Text log = new Text("Chat Log:");
        this.add(log, 0, 0, 2, 1);
        this.add(thisPlayer, 0, 1);
        this.add(thatPlayer, 1, 1);
    }

    /**
     *
     * @param msg The String message to send.
     */
    public void thisPlayerChat(String msg) {
        Text message = new Text(msg);
        message.setFill(control.getColor());
        thisPlayer.getChildren().add(message);
        thatPlayer.getChildren().add(new Text(""));
    }

    /**
     *
     * @param msg The String message to receive.
     * @param color The Color this message should be.
     */
    public void thatPlayerChat(String msg, Color color) {
        Text message = new Text(msg);
        message.setFill(color);
        thatPlayer.getChildren().add(message);
        thisPlayer.getChildren().add(new Text(""));
    }

    /**
     *
     * @param msg The String message to receive.
     */
    public void thatPlayerChat(String msg) {
        thatPlayerChat(msg, Color.BLACK);
    }

    /**
     *
     * @return The VBox chat box for this player.
     */
    public VBox getThisPlayer() {
        return thisPlayer;
    }

    /**
     *
     * @return The VBox chat box for the other player(s).
     */
    public VBox getThatPlayer() {
        return thatPlayer;
    }

    /**
     *
     * @return The HBox Chat builder.
     */
    public HBox getSendChat() {
        return sendChat;
    }

    /**
     *
     * @return The int width for the chat log.
     */
    public int getChatWidth() {
        return this.chatWidth;
    }
}
