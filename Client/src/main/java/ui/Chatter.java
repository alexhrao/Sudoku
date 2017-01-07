package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class Chatter extends HBox {
    private final TextField chatter = new TextField();
    private final Button sender = new Button("Send");

    /**
     * Creates a chatter with default width of 750
     */
    public Chatter() {
        this(750);
    }

    /**
     * Creates a chatter with specified width.
     * @param sendWidth The width of the chatter.
     */
    public Chatter(double sendWidth) {
        chatter.setMinWidth(sendWidth);
        this.getChildren().addAll(chatter, sender);
        this.setTranslateX(sendWidth / 7.5);
        sender.setMinSize(sendWidth / 50, sendWidth / 30);
        sender.setDisable(true);
    }

    /**
     * Gets the send button.
     * @return The send button.
     */
    public Button getSender() {
        return sender;
    }

    /**
     * Gets the chat field.
     * @return The chat bar.
     */
    public TextField getChatter() {
        return this.chatter;
    }


}
