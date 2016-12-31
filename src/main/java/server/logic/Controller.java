package main.java.server.logic;

import javafx.scene.paint.Color;
import main.java.server.ui.Square;

/**
 * The Controller class keeps the current state of the game, as seen from the vantage point of this user. It also keeps
 * track of information pertinent to this player, such as hosts and ports.
 */
public class Controller {
    private Square lastClicked;
    private String playerName;
    private Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private volatile int[][] solnBoard;
    private int serverPort;
    private String serverHost;
    private int spaces;
    private int id;

    /**
     * Constructs the controller for the current game, given the following parameters.
     * @param name The String name of this player.
     * @param color The Color of this player.
     * @param serverHost the String hostname of the server.
     * @param serverPort The int port of the server.
     */
    public Controller(String name, Color color, String serverHost, int serverPort) {
        this.playerName = name;
        this.playerColor = color;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    /**
     * Gets the last clicked square (as clicked by this user).
     * @return A Square that represents the last clicked square.
     */
    public Square getLastClicked() {
        return lastClicked;
    }

    /**
     * Gets this player's name.
     * @return The String name of this player.
     */
    public String getName() {
        return playerName;
    }

    /**
     * Gets this player's color.
     * @return A Color representing color of this player.
     */
    public Color getColor() {
        return playerColor;
    }

    /**
     * Returns whether or not this user is taking notes.
     * @return A boolean representing whether the player is in a note-taking state.
     */
    public boolean isNote() {
        return isNote;
    }

    /**
     * Returns whether or not this player is playing or paused.
     * @return A boolean representing whether the player is playing or paused.
     */
    public boolean isPlay() {
        return isPlay;
    }

    /**
     * Sets the given square to be the last clicked square.
     * @param square Set the given Square as the last clicked square.
     */
    public void setLastClicked(Square square) {
        this.lastClicked = square;
    }

    /**
     * Sets the note-taking state.
     * @param note Set the note-taking status to the given boolean.
     */
    public void setNote(boolean note) {
        this.isNote = note;
    }

    /**
     * Sets the playing state.
     * @param play Set the playing status to the given boolean
     */
    public void setPlay(boolean play) {
        this.isPlay = play;
    }

    /**
     * Gets the solution board; this is a synchronized method.
     * @return A double-layered int array of the solution board.
     */
    public synchronized int[][] getSolnBoard() {
        return solnBoard;
    }

    /**
     * Sets the solution board; this is a synchronized method.
     * @param solnBoard A double-layered int array of the solution board.
     */
    public synchronized void setSolnBoard(int[][] solnBoard) {
        this.solnBoard = solnBoard;
    }

    /**
     * Sets the server port.
     * @param port The server port, as an int.
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     * Gets the server port.
     * @return The server port, as an int.
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     *
     * @return The server host, as a String.
     */
    public String getServerHost() {
        return this.serverHost;
    }

    /**
     * Sets the server host.
     * @param host The server host, as a String.
     */
    public void setServerHost(String host) {
        this.serverHost = host;
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    public int getSpaces() {
        return this.spaces;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
