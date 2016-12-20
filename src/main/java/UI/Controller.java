package main.java.UI;

import javafx.scene.paint.Color;

/**
 * Created by alexh on 12/20/2016.
 */
public class Controller {
    private Square lastClicked;
    private int rowLastClicked = 0;
    private int colLastClicked = 0;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;

    public Controller() {
        this("Player 1", Color.RED);
    }
    public Controller(String name, Color color) {
        this.playerName = name;
        this.playerColor = color;
    }

    public Square getLastClicked() {
        return lastClicked;
    }

    public int getLastClickedRow() {
        return rowLastClicked;
    }

    public int getLastClickedCol() {
        return colLastClicked;
    }

    public String getName() {
        return playerName;
    }

    public Color getColor() {
        return playerColor;
    }

    public boolean getNote() {
        return isNote;
    }

    public boolean getPlay() {
        return isPlay;
    }

    public void setLastClicked(Square square) {
        this.lastClicked = square;
        this.rowLastClicked = square.getRow();
        this.colLastClicked = square.getCol();
    }


    public void setName(String name) {
        this.playerName = name;
    }

    public void setColor(Color color) {
        this.playerColor = color;
    }

    public void setNote(boolean note) {
        this.isNote = note;
    }

    public void setPlay(boolean play) {
        this.isPlay = play;
    }
}
