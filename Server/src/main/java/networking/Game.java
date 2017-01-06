package main.java.networking;

import javafx.scene.paint.Color;

import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class will be responsible for keeping games separate. It keeps track of this games players, colors, packets, etc.
 * In many ways, it represents what the server would look like, if the server could not have multiple games.
 */
public class Game implements Runnable {
    private ArrayList<SudokuServerThread> threads = new ArrayList<>();
    private int[][] board;
    private int[][] soln;
    private ArrayList<SudokuPacket> packets = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<Color> colors = new ArrayList<>();
    private String gameName;
    private SudokuServer server;

    public Game(Socket client, SudokuServer server) {
        this.server = server;
        // Eventually, we'll be passing the GAME, not the server!
        SudokuServerThread player = new SudokuServerThread(client, server);
        this.gameName = LocalDateTime.now() + ": " + player.getName();
        threads.add(player);
    }

    public void addPlayer(Socket client) {
        // Eventually, we'll be passing the GAME, not the server!
        SudokuServerThread player = new SudokuServerThread(client, this.server);
        threads.add(player);
    }

    public void removePlayer(int id) {
        names.set(id - 1, null);
        colors.set(id - 1, null);

        for (String name : names) {
            if (name != null) {
                return;
            }
        }
        server.removeGame(this);
    }

    public ArrayList<SudokuPacket> getPackets() {
        return this.packets;
    }

    public ArrayList<String> getNames() {
        return this.names;
    }

    public ArrayList<Color> getColors() {
        return this.colors;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int[][] getSoln() {
        return this.soln;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setSoln(int[][] soln) {
        this.soln = soln;
    }
    @Override
    public String toString() {
        return this.gameName + "'s Game.";
    }

    @Override
    public void run() {

    }
    /*
    Basically, this class should be a "mini-server" - a server for a single game. Each Game instance will be
    centered around 1 single game - and it's name should be the name of the player that started the game! (And number
    of free spaces? I don't know - perhaps toString Method can handle this!). From now on, a SudokuPacket that is
    requesting the server needs to either a) provide the number of spaces, b) specify which Game it wants to join, or
    c) query what Games are currently active.
    a)
        If it provides the number of spaces, then it wants to create a new game. We should create a Game with the name
        of the player as the creator, and create the new gameboard, etc.
    b)
        If it provides the Game it wishes to join, then the conversation (The Socket) should be passed to that game.
    c)
        If it queries the current Games, then it should return with a list of games and their identifiers.

    When we create a new game, we need a person who is the owner. Just give this as a string?
    We'll need to keep track of:
        * Players (Names, IDs, Colors, etc.)
        * Game History (the packet stack)
        *
     */


}
