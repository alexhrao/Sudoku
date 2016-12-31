package main.java.server.networking;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.server.ui.GameUI;
import main.java.server.ui.Square;
import org.jetbrains.annotations.Contract;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SudokuListener extends Thread implements Runnable {
    private GameUI ui;
    private String serverHost;
    private int serverPort;
    private Socket client;

    public SudokuListener(GameUI ui) {
        this("localhost", 60000, ui);
    }

    public SudokuListener(String host, int port, GameUI ui) {
        super("Listener");
        this.ui = ui;
        this.serverHost = host;
        this.serverPort = port;
    }

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
                SudokuPacket instruct;
                while ((instruct = (SudokuPacket) in.readObject()) != null) {
                    System.out.println("WE GOT DATA");
                    if (instruct.isBoard()) {
                        int[][] board = instruct.getBoard();
                        int[][] solnBoard = instruct.getSolnBoard();
                        ui.setSolnBoard(solnBoard);
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
                            System.out.println("Previous ID: " + ui.getControl().getId());
                            ui.getControl().setId(id);
                            System.out.println("Now my ID: " + ui.getControl().getId());
                        }
                        class AddName implements Runnable {
                            @Override
                            public void run() {
                                Text tName = new Text(name);
                                tName.setFill(Color.color(color[0], color[1], color[2], color[3]));
                                ui.getInfo().add(tName);
                                ui.getMenu().getSend().setDisable(true);
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
            System.out.println("Socket Closed!");
        } catch(EOFException e) {
            this.interrupt();
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Contract(pure = true)
    private boolean contains(int[] array, int token) {
        for (int i: array) {
            if (i == token) {
                return true;
            }
        }
        return false;
    }

    public Socket getClient() {
        return this.client;
    }
}
