package main.java.server.networking;

import main.java.server.logic.Controller;
import main.java.server.ui.Square;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.paint.Color;

public class SudokuClient implements Runnable {
    //TODO: Refactor this class.
    private String host;
    private int port;
    private SudokuProtocol translator;
    private Square[] squares;
    private int[][] board;
    private int[][] solnBoard;
    private boolean isBoard = false;
    private boolean isPlayer = false;
    private String playerName;
    private Color playerColor;
    private int id;
    private String message;
    private boolean isReturn = false;
    private boolean isMessage = false;
    private Color messageColor;
    private Controller control;

    public SudokuClient(Controller control, Square...squares) {
        this(control.getServerHost(), control.getServerPort(), squares);
        this.control = control;
    }

    public SudokuClient(Controller control, int[][] board, int[][] solnBoard) {
        this(control.getServerHost(), control.getServerPort(), board, solnBoard);
        this.control = control;
    }

    public SudokuClient(Controller control, String playerName, Color playerColor) {
        this(control.getServerHost(), control.getServerPort(), playerName, playerColor);
        this.control = control;
    }

    public SudokuClient(Controller control, String playerName, Color playerColor, boolean isReturn) {
        this(control.getServerHost(), control.getServerPort(), playerName, playerColor, isReturn);
        this.control = control;
    }

    public SudokuClient(Controller control, Color color, String message) {
        this(control.getServerHost(), control.getServerPort(), color, message);
        this.control = control;
    }

    private SudokuClient(String host, int port, int[][] board, int[][] solnBoard) {
        this.host = host;
        this.port = port;
        this.board = board;
        this.solnBoard = solnBoard;
        this.isBoard = true;
    }

    private SudokuClient(String host, int port, String playerName, Color playerColor) {
        this(host, port, playerName, playerColor, false);
    }

    private SudokuClient(String host, int port, String playerName, Color playerColor, boolean isReturn) {
        this.host = host;
        this.port = port;
        this.playerName = playerName;
        this.playerColor = playerColor;
        this.isPlayer = true;
        this.isReturn = isReturn;
    }

    private SudokuClient(String host, int port, Color color, String message) {
        this.host = host;
        this.port = port;
        this.message = message;
        this.isMessage = true;
        this.messageColor = color;
    }
    private SudokuClient(String host, int port, Square... squares) {
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
                translator = new SudokuProtocol(this.message, this.messageColor, control.getId(), true);
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
