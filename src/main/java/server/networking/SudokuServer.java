package main.java.server.networking;

import javafx.scene.paint.Color;
import main.java.server.ui.GameUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class SudokuServer implements Runnable {
    private String host;
    private int port;
    private volatile boolean isGoing = true;
    private volatile boolean firstPlayer = true;
    private ServerSocket server;
    private volatile ArrayList<SudokuServerThread> connections = new ArrayList<>();
    private volatile ArrayList<String> playerName = new ArrayList<>();
    private volatile ArrayList<Color> playerColor = new ArrayList<>();
    private volatile ArrayList<Integer> playerId = new ArrayList<>();
    private volatile ArrayList<SudokuPacket> packets = new ArrayList<>();
    private volatile int[][] board;
    private volatile int[][] soln;

    public SudokuServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread server = new Thread(new SudokuServer("localhost", 60000));
        server.start();
    }
    public void setGoing(boolean isGoing) {
        this.isGoing = isGoing;
    }

    public ServerSocket getServer() {
        return server;
    }

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public synchronized ArrayList<SudokuServerThread> getThreads() {
        return this.connections;
    }

    public synchronized boolean isFirstPlayer() {
        return this.firstPlayer;
    }

    public synchronized ArrayList<String> getPlayerName() {
        return this.playerName;
    }

    public synchronized ArrayList<Color> getPlayerColor() {
        return this.playerColor;
    }

    public synchronized int[][] getBoard() {
        return this.board;
    }

    public synchronized int[][] getSoln() {
        return this.soln;
    }

    public synchronized void setBoards(int[][] board, int[][] soln) {
        this.board = board;
        this.soln = soln;
        this.firstPlayer = false;
    }

    public synchronized int addPlayer(String name, Color color) {
        this.playerName.add(name);
        this.playerColor.add(color);
        this.playerId.add(this.playerColor.size());
        return this.playerColor.size();
    }

    public synchronized void removePlayer(int id) {
        this.playerName.set(id, null);
        this.playerColor.set(id, null);
        this.playerId.set(id, null);
        for (String name : this.playerName) {
            if (name != null) {
                return;
            }
        }
        this.board = new int[9][9];
        this.soln = this.board;
        this.firstPlayer = true;
    }

    public synchronized ArrayList<Integer> getPlayerId() {
        return this.playerId;
    }

    public synchronized void addPacket(SudokuPacket packet) {
        this.packets.add(packet);
    }

    public synchronized ArrayList<SudokuPacket> getPackets() {
        return this.packets;
    }

}
