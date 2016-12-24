package main.java.networking;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by alexh on 12/24/2016.
 */
public class SudokuServerThread extends Thread {
    private Socket client = null;

    public SudokuServerThread(Socket socket) {
        super("SudokuServerThread");
        this.client = socket;
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
                }
                System.out.println("AWWWW YEAAAAAAH");
            }
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
