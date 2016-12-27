package main.java.networking;

import main.java.ui.GameUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by alexh on 12/21/2016.
 */
public class SudokuServer implements Runnable {
    private String host;
    private int port;
    private GameUI ui;
    private volatile boolean isGoing = true;
    private ServerSocket server;

    public SudokuServer(String host, int port, GameUI ui) {
        this.host = host;
        this.port = port;
        this.ui = ui;

    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(port)) {
            this.server = server;
            while (isGoing) {
                try {
                    Thread thread = new SudokuServerThread(server.accept(), ui);
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
        // Thread game = new Thread(new Sudoku());
    }
    public void setGoing(boolean isGoing) {
        this.isGoing = isGoing;
    }

    public ServerSocket getServer() {
        return server;
    }
}
