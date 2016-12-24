package main.java.networking;

import main.java.ui.Square;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.paint.Color;

/**
 * Created by alexh on 12/21/2016.
 */
public class SudokuClient implements Runnable {
    private Socket client;
    private String host;
    private int port;
    private ObjectOutputStream out;
    private SudokuProtocol translator;
    private Square[] squares;

    public SudokuClient(Square... squares) {
        this("localhost", 60000, squares);
    }

    public SudokuClient(String host, int port, Square... squares) {
        this.host = host;
        this.port = port;
        this.squares = squares;
    }
    @Override
    public void run() {
        try (Socket client = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            translator = new SudokuProtocol(this.squares);
            out.writeObject(translator.getPacket());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSquares(Square... squares) {
        this.squares = squares;
    }

    public Square[] getSquares() {
        return this.squares;
    }
}
