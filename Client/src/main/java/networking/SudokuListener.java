package main.java.networking;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.ui.GameUI;
import main.java.ui.Square;
import main.java.networking.SudokuPacket.Data;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class is responsible for listening to the server, and in the eyes of the server, it's the player. It sends
 * information only at the beginning, then only listens.
 */
public class SudokuListener extends Thread implements Runnable {
    private GameUI ui;
    private String serverHost;
    private int serverPort;
    private Socket client;

    /**
     * Creates a default listener listening on localhost 60000.
     * @param ui The current GameUI.
     */
    public SudokuListener(GameUI ui) {
        this("localhost", 60000, ui);
    }

    /**
     * Creates a listener listening on the specified host and port.
     * @param host The server host.
     * @param port The server port.
     * @param ui The current GameUI.
     */
    public SudokuListener(String host, int port, GameUI ui) {
        super("Listener");
        this.ui = ui;
        this.serverHost = host;
        this.serverPort = port;
    }

    /**
     * Constantly listens to the server for more information. Must be stopped by closing the socket prematurely.
     */
    @Override
    public void run() {
        try (Socket client = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            this.client = client;
            out.flush();
            try (ObjectInputStream in = new ObjectInputStream(client.getInputStream())) {
                SudokuPacket packet = new SudokuPacket(ui.getControl().getSpaces(), ui.getControl().getName(),
                        ui.getControl().getColor());
                out.writeObject(packet);
                out.flush();
                SudokuPacket instruct;
                while ((instruct = (SudokuPacket) in.readObject()) != null) {
                    if (instruct.isBoard()) {
                        int[][] board = instruct.getBoard();
                        int[][] solnBoard = instruct.getSolnBoard();
                        ui.setSolnBoard(solnBoard);
                        ui.getControl().setBoard(board);
                        for (int row = 0; row < 9; row++) {
                            for (int col = 0; col < 9; col++) {
                                ui.getBoard().getSquare(row, col).clear();
                                ui.getBoard().getSquare(row, col).getAnswer().setValue(board[row][col]);
                                if (board[row][col] != 0) {
                                    ui.getBoard().getSquare(row, col).getAnswer().setVisible(true);
                                } else {
                                    ui.getBoard().getSquare(row, col).getAnswer().setVisible(false);
                                }
                            }
                        }
                    } else if (instruct.isPlayer()) {
                        String name = instruct.getName();
                        int id = instruct.getId();
                        double[] color = instruct.getColor();
                        if (ui.getControl().getId() == 0) {
                            ui.getControl().setId(id);
                        }
                        class AddName implements Runnable {
                            @Override
                            public void run() {
                                ui.getInfo().addPlayer(name, Color.color(color[0], color[1], color[2], color[3]));
                            }
                        }
                        Platform.runLater(new AddName());
                    } else if (instruct.isMessage()) {
                        if (instruct.getId() != ui.getControl().getId()) {
                            String message = instruct.getMessage();
                            double[] color = instruct.getColor();
                            class AddChat implements Runnable {
                                @Override
                                public void run() {
                                    ui.getChat().thatPlayerChat(message, Color.color(color[0], color[1], color[2], color[3]));
                                }
                            }
                            Platform.runLater(new AddChat());
                        }
                    } else if (instruct.isRemove()) {
                        int index = instruct.getId();
                        class RemovePlayer implements Runnable {
                            @Override
                            public void run() {
                                if (index != ui.getControl().getId()) {
                                    ui.getInfo().removePlayer(index);
                                }
                            }
                        }
                        Platform.runLater(new RemovePlayer());
                    } else {
                        if (ui.getControl().getLastClicked() != null) {
                            ui.getControl().getLastClicked().getOverlay().setStroke(ui.getControl().getColor());
                        }
                        for (int i = 0; i < instruct.getData().length; i++) {
                            Data datum = instruct.getData()[i];
                            int ans = datum.getAns();
                            int row = datum.getPosn()[0];
                            int col = datum.getPosn()[1];
                            int[] notes = datum.getNotes();
                            double[] ansColor = datum.getAnsColor();
                            double[] overColor = datum.getOverColor();
                            Square square = ui.getBoard().getSquare(row, col);
                            if (ans != 0) {
                                square.clear();
                                square.getAnswer().setVisible(true);
                                if (square == ui.getControl().getLastClicked() && ans == ui.getSolnBoard()[row][col]) {
                                    ui.getMenu().disable();
                                } else {
                                    ui.getMenu().enable();
                                }
                                square.getAnswer().setValue(ans);
                                square.getAnswer().setFill(Color.color(ansColor[0], ansColor[1], ansColor[2], ansColor[3]));
                            } else {
                                square.getAnswer().setVisible(false);
                                for (int n = 0; n < 9; n++) {
                                    if (contains(notes, n)) {
                                        square.getNotes().show(n);
                                    } else {
                                        square.getNotes().hide(n);
                                    }
                                }
                            }
                            square.getOverlay().setStroke(Color.color(overColor[0], overColor[1], overColor[2], overColor[3]));
                        }
                    }
                }
            }
        } catch (SocketException e) {
        } catch(EOFException e) {
            this.interrupt();
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private boolean contains(int[] array, int token) {
        for (int i: array) {
            if (i == token) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the client socket that this class listens with.
     * @return The client socket ("listener").
     */
    public Socket getClient() {
        return this.client;
    }
}
