package main.java.networking;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * This class is responsible for creating and maintaining the game. It primarily consists of passing sockets to threads,
 * but does keep a lot of accounting data.
 */
public class SudokuServer implements Runnable {
    private final int port;
    private volatile boolean isGoing = true;
    private volatile boolean firstPlayer = true;
    private ServerSocket server;
    private final ArrayList<SudokuServerThread> connections = new ArrayList<>();
    private final ArrayList<String> playerName = new ArrayList<>();
    private final ArrayList<Color> playerColor = new ArrayList<>();
    private final ArrayList<Integer> playerId = new ArrayList<>();
    private final ArrayList<Game> games = new ArrayList<>();
    private final ArrayList<SudokuPacket> packets = new ArrayList<>();
    private volatile int[][] board;
    private volatile int[][] soln;
    private static final int PORT = 60000;

    /**
     * Creates a Sudoku Server with the default port defined in PORT.
     */
    public SudokuServer() {
        this(PORT);
    }
    /**
     * Creates the Sudoku Server with the given host and port.
     * @param port The port.
     */
    public SudokuServer(int port) {
        this.port = port;
    }

    /**
     * Listens for connections, passing these connections to a new thread.
     */
    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            this.server = server;
            while (isGoing) {
                try {
                    SudokuServerThread thread = new SudokuServerThread(server.accept(), this);
                    thread.start();
                } catch (SocketException e) {
                    return;
                }
            }
        } catch (SocketException e) {
            System.out.println("Server Port " + port + " was not available.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the server. No arguments are allowed, but to stop the server, type in the console window.
     * @param args Not important.
     */
    public static synchronized void main(String[] args) {
        System.out.println("Starting Server...");
        SudokuServer sudokuServer = new SudokuServer(PORT);
        Thread server = new Thread(sudokuServer);
        System.out.println("Server Started. Listening for connections...");
        server.start();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            in.readLine();
            sudokuServer.getServer().close();
            System.out.println("Server has stopped.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    /**
//     * Sets the continuing condition.
//     * @param isGoing If the server should keep listening.
//     */
//    public synchronized void setGoing(boolean isGoing) {
//        this.isGoing = isGoing;
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)

    /**
     * Gets the serverSocket.
     * @return The currently utilized serverSocket.
     */
    private synchronized ServerSocket getServer() {
        return server;
    }

    /**
     * Gets all the player threads.
     * @return The connections.
     */
    public synchronized ArrayList<SudokuServerThread> getThreads() {
        return this.connections;
    }

    /**
     * Tells if this is the first player.
     * @return If this is the first player.
     */
    public synchronized boolean isFirstPlayer() {
        return this.firstPlayer;
    }

    /**
     * Gets the player names.
     * @return The player names (in order).
     */
    public synchronized ArrayList<String> getPlayerName() {
        return this.playerName;
    }

    /**
     * Gets the player colors.
     * @return The player colors (in order).
     */
    public synchronized ArrayList<Color> getPlayerColor() {
        return this.playerColor;
    }

    /**
     * Gets the board.
     * @return The board.
     */
    public synchronized int[][] getBoard() {
        return this.board;
    }

    /**
     * Gets the solution board.
     * @return The solution board.
     */
    public synchronized int[][] getSoln() {
        return this.soln;
    }

    /**
     * Sets the boards. This will automatically falsify the first player.
     * @param board The board.
     * @param soln The solution.
     */
    public synchronized void setBoards(int[][] board, int[][] soln) {
        this.board = board;
        this.soln = soln;
        this.firstPlayer = false;
    }

    /**
     * Adds the specified player.
     * @param name The player name.
     * @param color The player color.
     * @return The player's ID.
     */
    public int addPlayer(String name, Color color) {
        this.playerName.add(name);
        this.playerColor.add(color);
        this.playerId.add(this.playerColor.size());
        return this.playerColor.size();
    }

    /**
     * Removes the specified player.
     * @param id The player to remove.
     */
    public void removePlayer(int id) {
        this.playerName.set(id - 1, null);
        this.playerColor.set(id - 1, null);
        this.playerId.set(id - 1, null);
        for (String name : this.playerName) {
            if (name != null) {
                return;
            }
        }
        this.board = new int[9][9];
        this.soln = this.board;
        this.firstPlayer = true;
        this.packets.clear();
        this.playerColor.clear();
        this.playerId.clear();
        this.playerName.clear();
        this.connections.clear();
        System.out.println("No Active Players - Game reset.");
    }

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    public void resetGame() {
//        this.board = new int[9][9];
//        this.soln = this.board;
//        this.firstPlayer = true;
//        this.packets.clear();
//        this.playerColor.clear();
//        this.playerId.clear();
//        this.playerName.clear();
//        this.connections.clear();
//        System.out.println("No Active Players - Game reset.");
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    /**
//     * Gets the playerID collection.
//     * @return The collection of player IDs.
//     */
//    public ArrayList<Integer> getPlayerId() {
//        return this.playerId;
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)

    /**
     * Adds a packet to the stack.
     * @param packet The packet to add.
     */
    public synchronized void addPacket(SudokuPacket packet) {
        this.packets.add(packet);
    }

    /**
     * Gets all the current SudokuPackets.
     * @return All Sudoku Packets since the beginning of the game.
     */
    public synchronized ArrayList<SudokuPacket> getPackets() {
        return this.packets;
    }

    public synchronized void removeGame(Game game) {
        games.remove(game);
    }

    public synchronized ArrayList<Game> getGames() {
        return this.games;
    }
}
