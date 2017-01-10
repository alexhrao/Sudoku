package main.java.networking;

import main.java.logic.Controller;
import main.java.ui.Square;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javafx.scene.paint.Color;

/**
 * This class sends information to the server to be sent off to all other players. It creates a packet using the given
 * data, then sends off the packet to be distributed.
 */
public class SudokuSender implements Runnable {
    private Square[] squares;
    private String message;
    private boolean isMessage = false;
    private boolean isRemove = false;
    private boolean isGame = false;
    private Color messageColor;
    private final Controller control;

    /**
     * This constructor makes a packet for Squares.
     * @param control The current controller.
     * @param squares The modified Squares.
     */
    public SudokuSender(Controller control, Square...squares) {
        this.control = control;
        this.squares = squares;
    }

    /**
     * This constructor makes a packet for messages.
     * @param control The current controller.
     * @param color The color of the message.
     * @param message The content of the message.
     */
    public SudokuSender(Controller control, Color color, String message) {
        this.control = control;
        this.message = message;
        this.isMessage = true;
        this.messageColor = color;
    }

    /**
     * This constructor makes a packet for removing a player.
     * @param control The controller.
     */
    public SudokuSender(Controller control) {
        this.control = control;
        this.isRemove = true;
    }

    /**
     * Creates a sender to get games.
     * @param control The current controller.
     * @param isGame I
     */
    public SudokuSender(Controller control, boolean isGame) {
        this.control = control;
        this.isGame = true;
    }

    /**
     * This method creates the packet, then sends it off to the server for distribution.
     */
    @Override
    public void run() {
        try (Socket client = new Socket(control.getServerHost(), control.getServerPort());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
            SudokuPacket packet;
            if (isGame) {
                packet = new SudokuPacket();
            } else if (isMessage) {
                packet = new SudokuPacket(this.message, this.messageColor, control.getId(), true);
            } else if (isRemove) {
                packet = new SudokuPacket(control.getId());
            } else {
                packet = new SudokuPacket(this.squares);
            }
            out.writeObject(packet);
            out.flush();
            try {
                in.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
