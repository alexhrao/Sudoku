package main.java.networking;

import main.java.logic.Controller;
import main.java.ui.Square;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.paint.Color;

public class SudokuClient implements Runnable {
    private Square[] squares;
    private String message;
    private boolean isMessage = false;
    private boolean isRemove = false;
    private Color messageColor;
    private Controller control;

    public SudokuClient(Controller control, Square...squares) {
        this.control = control;
        this.squares = squares;
    }
    public SudokuClient(Controller control, Color color, String message) {
        this.control = control;
        this.message = message;
        this.isMessage = true;
        this.messageColor = color;
    }
    public SudokuClient(Controller control) {
        this.control = control;
        this.isRemove = true;
    }

    @Override
    public void run() {
        try (Socket client = new Socket(control.getServerHost(), control.getServerPort());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            SudokuPacket packet;
            if (isMessage) {
                packet = new SudokuPacket(this.message, this.messageColor, control.getId(), true);
            } else if (isRemove) {
                packet = new SudokuPacket(control.getId());
            } else {
                packet = new SudokuPacket(this.squares);
            }
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
