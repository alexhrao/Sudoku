package main.java.logic;

import javafx.scene.paint.Color;
import main.java.ui.Square;

/**
 * The Controller class keeps the current state of the game, as seen from the vantage point of this user. It also keeps
 * track of information pertinent to this player, such as hosts and ports.
 */
public class Controller {
    private Square lastClicked;
    private final String playerName;
    private final Color playerColor;
    private boolean isNote;
    private boolean isPlay;
    private volatile int[][] board;
    private volatile int[][] solnBoard;
    private final int serverPort;
    private final String serverHost;
    private int spaces;
    private int id;
    private SudokuLoader loader;
    private String[] input = new String[9];

    /**
     * Constructs the controller for the current game, given the following parameters.
     *
     * @param name       The String name of this player.
     * @param color      The Color of this player.
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
     *
     * @return A Square that represents the last clicked square.
     */
    public Square getLastClicked() {
        return lastClicked;
    }

    /**
     * Gets this player's name.
     *
     * @return The String name of this player.
     */
    public String getName() {
        return playerName;
    }

    /**
     * Gets this player's color.
     *
     * @return A Color representing color of this player.
     */
    public Color getColor() {
        return playerColor;
    }

    /**
     * Returns whether or not this user is taking notes.
     *
     * @return A boolean representing whether the player is in a note-taking state.
     */
    public boolean isNote() {
        return isNote;
    }

    /**
     * Returns whether or not this player is playing or paused.
     *
     * @return A boolean representing whether the player is playing or paused.
     */
    public boolean isPlay() {
        return isPlay;
    }

    /**
     * Sets the given square to be the last clicked square.
     *
     * @param square Set the given Square as the last clicked square.
     */
    public void setLastClicked(Square square) {
        this.lastClicked = square;
    }

    /**
     * Sets the note-taking state.
     *
     * @param note Set the note-taking status to the given boolean.
     */
    public void setNote(boolean note) {
        this.isNote = note;
    }

    /**
     * Sets the playing state.
     *
     * @param play Set the playing status to the given boolean
     */
    public void setPlay(boolean play) {
        this.isPlay = play;
    }

    /**
     * Gets the solution board; this is a synchronized method.
     *
     * @return A double-layered int array of the solution board.
     */
    public synchronized int[][] getSolnBoard() {
        return solnBoard;
    }

    /**
     * Sets the solution board; this is a synchronized method.
     *
     * @param solnBoard A double-layered int array of the solution board.
     */
    public synchronized void setSolnBoard(int[][] solnBoard) {
        this.solnBoard = solnBoard;
    }

    /**
     * Gets the server port.
     *
     * @return The server port, as an int.
     */
    public int getServerPort() {
        return this.serverPort;
    }

    /**
     * @return The server host, as a String.
     */
    public String getServerHost() {
        return this.serverHost;
    }

    /**
     * Set the number of requested spaces.
     *
     * @param spaces The number of free spaces.
     */
    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    /**
     * Get the number of requested spaces.
     *
     * @return The number of free spaces requested.
     */
    public int getSpaces() {
        return this.spaces;
    }

    /**
     * Get this player's ID.
     *
     * @return The current ID.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set this player's ID.
     *
     * @param id This player's ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set the current board.
     *
     * @param board The current board.
     */
    public void setBoard(int[][] board) {
        this.board = board;
    }

    /**
     * Get the current board.
     *
     * @return The current board.
     */
    public int[][] getBoard() {
        return this.board;
    }

    public void setLoader(SudokuLoader loader)  {
        this.loader = loader;
    }

    public void ready() {
        loader.ready();
    }

    public void setInput(String[] input) {
        this.input = input;
    }

    public String[] getInput() {
        return this.input;
    }
}
