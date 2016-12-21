package main.java.UI;

import javafx.scene.paint.Color;

class Controller {
    private Square lastClicked;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private int[][] board;
    private int[][] soln;

    Controller() {
        this("Player 1", Color.RED);
    }
    Controller(String name, Color color) {
        this.playerName = name;
        this.playerColor = color;
    }

    Square getLastClicked() {
        return lastClicked;
    }


    String getName() {
        return playerName;
    }

    Color getColor() {
        return playerColor;
    }

    boolean getNote() {
        return isNote;
    }

    boolean getPlay() {
        return isPlay;
    }

    void setLastClicked(Square square) {
        this.lastClicked = square;
    }

    void setNote(boolean note) {
        this.isNote = note;
    }

    void setPlay(boolean play) {
        this.isPlay = play;
    }

    void setBoard(int[][] board) {
        this.board = board;
    }

    int[][] getSoln() {
        return soln;
    }

    void setSoln(int[][] soln) {
        this.soln = soln;
    }
}
