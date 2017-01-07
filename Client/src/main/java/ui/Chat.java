package main.java.ui;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import main.java.logic.Controller;

public class Chat extends GridPane {
    public static final int SPACING = 20;
    private final Controller control;
    private final VBox thisPlayer;
    private final VBox thatPlayer;
    private final double chatWidth;

    /**
     * Creates a new Chat with default width of 300.
     * @param control The controller.
     */
    public Chat(Controller control) {
        this(300, control);
    }

    /**
     * Creates a new Chat with the given width.
     * @param chatWidth A preferred int width for the chat box.
     * @param control The controller.
     */
    public Chat(double chatWidth, Controller control) {
        thisPlayer = new VBox(SPACING);
        thisPlayer.setMinWidth(chatWidth / 2);
        thatPlayer = new VBox(SPACING);
        thatPlayer.setMinWidth(chatWidth / 2);
        this.chatWidth = chatWidth;
        this.control = control;
        this.setMinWidth(chatWidth);
        Text log = new Text("Chat Log:");
        this.add(log, 0, 0, 2, 1);
        this.add(thisPlayer, 0, 1);
        this.add(thatPlayer, 1, 1);
    }

    /**
     * Chats as this player.
     * @param msg The String message to send.
     */
    public void thisPlayerChat(String msg) {
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Text message = new Text(msg);
        message.setFill(control.getColor());
        message.setWrappingWidth(thisPlayer.getMinWidth() - 2);
        message.setFont(new Font(screenHeight / 65));
        message.setTextAlignment(TextAlignment.LEFT);
        thisPlayer.getChildren().add(message);
        Text spacer = new Text(msg);
        spacer.setFill(Color.TRANSPARENT);
        spacer.setWrappingWidth(thatPlayer.getMinWidth() - 2);
        spacer.setFont(new Font(screenHeight / 65));
        spacer.setTextAlignment(TextAlignment.RIGHT);
        thatPlayer.getChildren().add(spacer);
    }

    /**
     * Chats as a different player.
     * @param msg The String message to receive.
     * @param color The Color this message should be.
     */
    public void thatPlayerChat(String msg, Color color) {
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Text message = new Text(msg);
        message.setFill(color);
        message.setWrappingWidth(thatPlayer.getMinWidth() - 2);
        message.setFont(new Font(screenHeight / 65));
        message.setTextAlignment(TextAlignment.RIGHT);
        thatPlayer.getChildren().add(message);
        Text spacer = new Text(msg);
        spacer.setFill(Color.TRANSPARENT);
        spacer.setWrappingWidth(thisPlayer.getMinWidth() - 2);
        spacer.setFont(new Font(screenHeight / 65));
        spacer.setTextAlignment(TextAlignment.LEFT);
        thisPlayer.getChildren().add(spacer);
    }

    /**
     *
     * @return The width for the chat log.
     */
    public double getChatWidth() {
        return this.chatWidth;
    }
}
