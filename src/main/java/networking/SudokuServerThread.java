package main.java.networking;

import javafx.scene.paint.Color;
import main.java.ui.GameUI;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by alexh on 12/24/2016.
 */
public class SudokuServerThread extends Thread {
    private Socket client = null;
    private GameUI ui;

    public SudokuServerThread(Socket socket, GameUI ui) {
        super("SudokuServerThread");
        this.client = socket;
        this.ui = ui;
    }

    @Override
    public void run() {
        try (ObjectInputStream reader = new ObjectInputStream(client.getInputStream())) {
            Object data;
            SudokuPacket instruct;
            while ((data = reader.readObject()) != null) {
                instruct = (SudokuPacket) data;
                for(int i = 0; i < instruct.getData().length; i++) {
                    System.out.println(instruct.getData()[i].getPosn()[0] + "");
                    ui.getBoard().getSquare(instruct.getData()[i].getPosn()[0] + 1, instruct.getData()[i].getPosn()[1] + 1)
                            .getOverlay().setStroke(Color.BLUE);
                }
                System.out.println("AWWWW YEAAAAAAH");
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
