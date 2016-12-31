package main.java.ui;

import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class InfoMenu extends ToolBar {
    private Text[] name;
    private Color[] color;
    private Text pause = new Text("Playing...");

    public InfoMenu() {
        this.setPrefHeight(25);
        this.getItems().add(pause);
    }

    public InfoMenu(String name, Color color) {
        this(toArr(name), toArr(color));
    }
    public InfoMenu(String[] name, Color[] color) {
        this();
        this.name = new Text[name.length];
        for (int k = 0; k < name.length; k++) {
            this.name[k] = new Text(name[k]);
            this.name[k].setFill(color[k]);
        }
        this.color = color;

        this.getItems().add(new Text("   "));
        this.getItems().add(this.name[0]);
        for (int k = 1; k < this.name.length; k++) {
            this.getItems().add(new Text("   "));
            this.getItems().add(this.name[k]);
        }
    }

    public Text getName(int player) {
        return name[player - 1];
    }

    public void setName(int player, String name) {
        this.name[player - 1] = new Text(name);
    }

    public Color getColor(int player) {
        return color[player - 1];
    }

    public void setColor(int player, Color color) {
        this.color[player - 1] = color;
    }

    public void add(Node node) {
        this.getItems().add(new Text("   "));
        this.getItems().add(node);
    }

    public void setPause(boolean isPause) {
        if (isPause) {
            this.pause.setText("Paused...");
        } else {
            this.pause.setText("Playing...");
        }
    }

    public void removePlayer(int index) {
        this.getItems().set((2 * index) + 1, new Text(""));
        this.getItems().set((2 * index) + 2, new Text(""));
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
