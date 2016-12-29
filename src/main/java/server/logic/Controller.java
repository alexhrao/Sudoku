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

    /**
     *
     * @param name The String name of this player.
     * @param color The Color of this player.
     * @param serverHost the String hostname of the server.
     * @param clientHost The String hostname of the client.
     * @param serverPort The int port of the server.
     * @param clientPort The int port of the client.
     */
    public Controller(String name, Color color, String serverHost, String clientHost, int serverPort, int clientPort) {
        this.playerName = name;
        this.playerColor = color;
        this.serverHost = serverHost;
        this.clientHost = clientHost;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    /**
     *
     * @return A Square that represents the last clicked square.
     */
    public Square getLastClicked() {
        return lastClicked;
    }

    /**
     *
     * @return The String name of this player.
     */
    public String getName() {
        return playerName;
    }

    /**
     *
     * @return A Color representing color of this player.
     */
    public Color getColor() {
        return playerColor;
    }

    /**
     *
     * @return A boolean representing whether the player is in a note-taking state.
     */
    public boolean isNote() {
        return isNote;
    }

    /**
     *
     * @return A boolean representing whether the player is playing or paused.
     */
    public boolean isPlay() {
        return isPlay;
    }

    /**
     *
     * @param square Set the given Square as the last clicked square.
     */
    public void setLastClicked(Square square) {
        this.lastClicked = square;
    }

    /**
     *
     * @param note Set the note-taking status to the given boolean.
     */
    public void setNote(boolean note) {
        this.isNote = note;
    }

    /**
     *
     * @param play Set the playing status to the given boolean
     */
    public void setPlay(boolean play) {
        this.isPlay = play;
    }

    /**
     *
     * @return A double-layered int array of the solution board.
     */
    public int[][] getSolnBoard() {
        return solnBoard;
    }

    /**
     *
     * @param solnBoard A double-layered int array of the solution board.
     */
    public void setSolnBoard(int[][] solnBoard) {
        this.solnBoard = solnBoard;
    }

    /**
     *
     * @param port The server port, as an int.
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     *
     * @return The server port, as an int.
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     *
     * @param port the client port, as an int.
     */
    public void setClientPort(int port) {
        this.clientPort = port;
    }

    /**
     *
     * @return The client port, as an int.
     */
    public int getClientPort() {
        return this.clientPort;
    }

    /**
     *
     * @return The server host, as a String.
     */
    public String getServerHost() {
        return this.serverHost;
    }

    /**
     *
     * @return The client host, as a String.
     */
    public String getClientHost() {
        return this.clientHost;
    }
}
