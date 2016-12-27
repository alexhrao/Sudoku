package main.java.logic;

import javafx.scene.paint.Color;
import main.java.ui.Square;

public class Controller {
    private Square lastClicked;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private volatile int[][] board;
    private volatile int[][] soln;
    private int sPort;
    private int cPort;
    private String sHost;
    private String cHost;

    public Controller() {
        this("Player 1", Color.RED, "localhost", "localhost", 1025, 1026);
    }
    public Controller(String name, Color color, String sHost, String cHost, int sPort, int cPort) {
        this.playerName = name;
        this.playerColor = color;
        this.sHost = sHost;
        this.cHost = cHost;
        this.sPort = sPort;
        this.cPort = cPort;
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

    public void setServerPort(int port) {
        this.sPort = port;
    }

    public int getServerPort() {
        return this.sPort;
    }

    public void setClientPort(int port) {
        this.cPort = port;
    }

    public int getClientPort() {
        return this.cPort;
    }

    public String getServerHost() {
        return this.sHost;
    }

    public String getClientHost() {
        return this.cHost;
    }
}
