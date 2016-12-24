package main.java.networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by alexh on 12/21/2016.
 */
public class SudokuServer implements Runnable {
    private String host;
    private int port;

    public SudokuServer() {
        this("localhost", 60000);
    }
    public SudokuServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Thread thread = new SudokuServerThread(server.accept());
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SudokuServer server = new SudokuServer();
        server.run();
    }
}
