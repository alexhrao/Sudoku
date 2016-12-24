package main.java.logic;

import javafx.scene.paint.Color;
import main.java.ui.Square;

public class Controller {
    private Square lastClicked;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private int[][] board;
    private int[][] soln;

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
    }

    public void setNote(boolean note) {
        this.isNote = note;
    }

    public void setPlay(boolean play) {
        this.isPlay = play;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int[][] getSoln() {
        return soln;
    }

    public void setSoln(int[][] soln) {
        this.soln = soln;
    }
}
