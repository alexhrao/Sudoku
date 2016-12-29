package main.java.networking;

import main.java.logic.Controller;
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
    private String host;
    private int port;
    private ObjectOutputStream out;
    private SudokuProtocol translator;
    private Square[] squares;
    private int[][] board;
    private int[][] solnBoard;
    private boolean isBoard = false;
    private boolean isPlayer = false;
    private String playerName;
    private Color playerColor;
    private String message;
    private boolean isReturn = false;
    private boolean isMessage = false;
    private Color messageColor;

    public SudokuClient(Controller control, Square...squares) {
        this(control.getClientHost(), control.getClientPort(), squares);
    }

    public SudokuClient(Controller control, int[][] board, int[][] solnBoard) {
        this(control.getClientHost(), control.getClientPort(), board, solnBoard);
    }

    public SudokuClient(Controller control, String playerName, Color playerColor) {
        this(control.getClientHost(), control.getClientPort(), playerName, playerColor);
    }

    public SudokuClient(Controller control, String playerName, Color playerColor, boolean isReturn) {
        this(control.getClientHost(), control.getClientPort(), playerName, playerColor, isReturn);
    }

    public SudokuClient(Controller control, Color color, String message) {
        this(control.getClientHost(), control.getClientPort(), color, message);
    }

    public SudokuClient(String host, int port, int[][] board, int[][] solnBoard) {
        this.host = host;
        this.port = port;
        this.board = board;
        this.solnBoard = solnBoard;
        this.isBoard = true;
    }

    public SudokuClient(String host, int port, String playerName, Color playerColor) {
        this(host, port, playerName, playerColor, false);
    }

    public SudokuClient(String host, int port, String playerName, Color playerColor, boolean isReturn) {
        this.host = host;
        this.port = port;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.isPlayer = true;
        this.isReturn = isReturn;
    }

    public SudokuClient(String host, int port, Color color, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        this.isMessage = true;
        this.messageColor = color;
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
            if (isBoard) {
                translator = new SudokuProtocol(this.board, this.solnBoard);
            } else if (isPlayer) {
                translator = new SudokuProtocol(this.playerName, this.playerColor, this.isReturn);
            } else if (isMessage) {
                translator = new SudokuProtocol(this.message, this.messageColor);
            } else {
                translator = new SudokuProtocol(this.squares);
            }
            out.writeObject(translator.getPacket());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
