package main.java.ui;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class InfoMenu extends ToolBar {
    private Text pause = new Text("Playing...");
    private GameUI ui;

    public InfoMenu(GameUI ui) {
        this.setPrefHeight(25);
        this.getItems().add(pause);
        this.ui = ui;
    }

    public InfoMenu(String name, Color color, GameUI ui) {
        this(toArr(name), toArr(color), ui);
    }
    public InfoMenu(String[] name, Color[] color, GameUI ui) {
        this(ui);
        this.getItems().add(new HBox(20, pause));
        for (int k = 0; k < name.length; k++) {
            Text item = new Text(name[k]);
            item.setFill(color[k]);
            HBox person = new HBox(20, item);
            this.getItems().add(person);
        }
    }

    public void add(Node node) {
        this.getItems().add(node);
    }

    public void addPlayer(String name, Color color) {
        Text person = new Text(name);
        person.setFill(color);
        this.getItems().addAll(new HBox(20, person));
    }

    public void setPause(boolean isPause) {
        if (isPause) {
            this.pause.setText("Paused...");
        } else {
            this.pause.setText("Playing...");
        }
    }

    public void removePlayer(int index) {
        if (index > ui.getControl().getId()) {
            this.getItems().set(index, new HBox(20));
        } else {
            this.getItems().set(index + 1, new HBox(20));
        }

    }

    private static String[] toArr(String in) {
        String[] out = {in};
        return out;
    }

    private static Color[] toArr(Color in) {
        Color[] out = {in};
        return out;
    }

}
