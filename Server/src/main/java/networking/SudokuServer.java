package main.java.networking;

import javafx.scene.paint.Color;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
//TODO: Add removal of player!

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
    private static final String HOST = "localhost";
    private static final int PORT = 60000;

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
        } catch (SocketException e) {
            System.out.println("Server Port closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SudokuServer sudokuServer = new SudokuServer(HOST, PORT);
        Thread server = new Thread(sudokuServer);
        server.start();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("quit")) {
                    sudokuServer.getServer().close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public ArrayList<SudokuServerThread> getThreads() {
        return this.connections;
    }

    public boolean isFirstPlayer() {
        return this.firstPlayer;
    }

    public ArrayList<String> getPlayerName() {
        return this.playerName;
    }

    public ArrayList<Color> getPlayerColor() {
        return this.playerColor;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int[][] getSoln() {
        return this.soln;
    }

    public void setBoards(int[][] board, int[][] soln) {
        this.board = board;
        this.soln = soln;
        this.firstPlayer = false;
    }

    public int addPlayer(String name, Color color) {
        this.playerName.add(name);
        this.playerColor.add(color);
        this.playerId.add(this.playerColor.size());
        return this.playerColor.size();
    }

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
    }

    public ArrayList<Integer> getPlayerId() {
        return this.playerId;
    }

    public void addPacket(SudokuPacket packet) {
        this.packets.add(packet);
    }

    public ArrayList<SudokuPacket> getPackets() {
        return this.packets;
    }

}
