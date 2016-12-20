package main.java.UI;

import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created by alexh on 12/20/2016.
 */
public class InfoMenu extends ToolBar {
    private Text name = new Text("Player 1");
    private Color color = new Color(1, 0, 0, 1);

    public InfoMenu() {
        this.setPrefHeight(50);
        name.setStroke(color);
        this.getItems().add(name);
    }

    public Text getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
