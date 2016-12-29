package main.java.server.ui;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class Chatter extends HBox {
    TextField chatter = new TextField("Hello World!");
    Button sender = new Button("Send");

    public Chatter() {
        this(750);
    }
    public Chatter(int sendWidth) {
        chatter.setMinWidth(sendWidth);
        this.getChildren().addAll(chatter, sender);
        this.setTranslateX(100);
    }

    public Button getSender() {
        return sender;
    }

    public TextField getChatter() {
        return this.chatter;
    }


}
