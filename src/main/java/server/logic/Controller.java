package main.java.server.logic;

import javafx.scene.paint.Color;
import main.java.server.ui.Square;

public class Controller {
    private Square lastClicked;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private volatile int[][] solnBoard;
    private int serverPort;
    private int clientPort;
    private String serverHost;
    private String clientHost;

    public Controller(String name, Color color, String serverHost, String clientHost, int serverPort, int clientPort) {
        this.playerName = name;
        this.playerColor = color;
        this.serverHost = serverHost;
        this.clientHost = clientHost;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
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

    public int[][] getSolnBoard() {
        return solnBoard;
    }

    public void setSolnBoard(int[][] solnBoard) {
        this.solnBoard = solnBoard;
    }

    public void setServerPort(int port) {
        this.serverPort = port;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public void setClientPort(int port) {
        this.clientPort = port;
    }

    public int getClientPort() {
        return this.clientPort;
    }

    public String getServerHost() {
        return this.serverHost;
    }

    public String getClientHost() {
        return this.clientHost;
    }
}
