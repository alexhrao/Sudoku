package main.java.server.networking;

import main.java.server.ui.GameUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class SudokuListener extends Thread implements Runnable {
    private GameUI ui;
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 60000;
    public SudokuListener() {

    }
    public SudokuListener(GameUI ui) {
        super("Listener");
        this.ui = ui;
    }
    @Override
    public void run() {
        try (Socket client = new Socket(SERVER_HOST, SERVER_PORT);
             ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
            String data;
            while ((data = (String) in.readObject()) != null) {
                System.out.println("Data " + data + " Received.");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SudokuListener tester = new SudokuListener();
        tester.start();
    }

}
