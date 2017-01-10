package main.java.ui;

import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * This class is responsible for creating and maintaining the ToolBar that holds player names.
 */
public class InfoMenu extends ToolBar {
    private final Text pause = new Text("Playing...");
    private final GameUI ui;

    /**
     * Create a default ToolBar with the pause indicator and height of 25.
     * @param ui The current GameUI.
     */
    public InfoMenu(GameUI ui) {
        this(ui, 25);
    }

    /**
     * Create a ToolBar with specified height.
     * @param ui The current GameUI.
     * @param height The height.
     */
    public InfoMenu(GameUI ui, double height) {
        this.setPrefHeight(height);
        this.getItems().add(pause);
        this.ui = ui;
    }

    /**
     * Creates a ToolBar with the given player names and colors.
     * @param name A String array of player names.
     * @param color A String array of player colors.
     * @param ui The current GameUI.
     */
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

    /**
     * Add a new player.
     * @param name The player name.
     * @param color The player color.
     */
    public void addPlayer(String name, Color color) {
        Text person = new Text(name);
        person.setFill(color);
        this.getItems().addAll(new HBox(20, person));
    }

    /**
     * Play or Pause the game.
     * @param isPause If the game is paused.
     */
    public void setPause(boolean isPause) {
        if (isPause) {
            this.pause.setText("Paused...");
        } else {
            this.pause.setText("Playing...");
        }
    }

    /**
     * Remove a player, given the player's ID.
     * @param id The ID of the removed player.
     */
    public void removePlayer(int id) {
        if (id > ui.getControl().getId()) {
            this.getItems().set(id, new HBox(20));
        } else {
            this.getItems().set(id + 1, new HBox(20));
        }
    }
}
