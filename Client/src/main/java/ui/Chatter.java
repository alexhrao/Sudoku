package main.java.ui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class Chatter extends HBox {
    private final TextField chatter = new TextField("Hello World!");
    private final Button sender = new Button("Send");

    public Chatter() {
        this(750);
    }
    private Chatter(int sendWidth) {
        chatter.setMinWidth(sendWidth);
        this.getChildren().addAll(chatter, sender);
        this.setTranslateX(100);
        sender.setMinSize(50, 25);
    }

    public Button getSender() {
        return sender;
    }

    public TextField getChatter() {
        return this.chatter;
    }


}
