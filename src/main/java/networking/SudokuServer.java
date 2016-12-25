package main.java.networking;

import javafx.application.Application;
import main.java.ui.GameUI;
import main.java.ui.Sudoku;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alexh on 12/21/2016.
 */
public class SudokuServer implements Runnable {
    private String host;
    private int port;
    private GameUI ui;
    private volatile boolean isGoing = true;

    public SudokuServer(String host, int port, GameUI ui) {
        this.host = host;
        this.port = port;
        this.ui = ui;
    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(port)) {
            while (isGoing) {
                Thread thread = new SudokuServerThread(server.accept(), ui);
                thread.start();
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
}
