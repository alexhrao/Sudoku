package main.java.networking;

import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import main.java.ui.GameUI;
import main.java.ui.Notes;
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
    private final GameUI ui;
    private final String serverHost;
    private final int serverPort;
    private Socket client;

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
                SudokuPacket packet;
                if (ui.getControl().getInput()[0] != null) {
                    packet = new SudokuPacket(ui.getControl().getInput(), ui.getControl().getName(),
                            ui.getControl().getColor());
                } else {
                    packet = new SudokuPacket(ui.getControl().getSpaces(), ui.getControl().getName(),
                            ui.getControl().getColor());
                }
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
                        Platform.runLater(() ->  ui.getInfo().addPlayer(name, Color.color(color[0], color[1], color[2], color[3])));
                    } else if (instruct.isMessage()) {
                        if (instruct.getId() != ui.getControl().getId()) {
                            String message = instruct.getMessage();
                            double[] color = instruct.getColor();
                            Platform.runLater(() -> {
                                ui.getChat().thatPlayerChat(message, Color.color(color[0], color[1], color[2], color[3]));
                                ui.scrollToBottom();
                            });
                        }
                    } else if (instruct.isRemove()) {
                        int index = instruct.getId();
                        Platform.runLater(() -> {
                            if (index != ui.getControl().getId()) {
                                ui.getInfo().removePlayer(index);
                            }
                        });
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
                                if (square == ui.getControl().getLastClicked()) {
                                    if (ans == ui.getSolnBoard()[row][col]) {
                                        ui.getMenu().disable();
                                    } else {
                                        ui.getMenu().enable();
                                    }
                                }
                                for (int r = 0; r < 9; r++) {
                                    if (r != square.getRow()) {
                                        Notes posNotes = ui.getBoard().getSquare(r, square.getCol()).getNotes();
                                        posNotes.hide(ans - 1);
                                    }
                                }
                                for (int c = 0; c < 9; c++) {
                                    if (c != square.getCol()) {
                                        Notes posNotes = ui.getBoard().getSquare(square.getRow(), c).getNotes();
                                        posNotes.hide(ans - 1);
                                    }
                                }
                                for (int r = (int) Math.floor(square.getRow() / 3) * 3; r < (Math.floor(square.getRow() / 3) * 3) + 3; r++) {
                                    for (int c = (int) Math.floor(square.getCol() / 3) * 3; c < (Math.floor(square.getCol() / 3) * 3) + 3; c++) {
                                        if (r != square.getRow() || c != square.getCol()) {
                                            Notes posNotes = ui.getBoard().getSquare(r, c).getNotes();
                                            posNotes.hide(ans - 1);
                                        }
                                    }
                                }
                                square.getAnswer().setValue(ans);
                                square.getAnswer().setStroke(Color.color(ansColor[0], ansColor[1], ansColor[2], ansColor[3]));
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
                            Color color = Color.color(overColor[0], overColor[1], overColor[2], overColor[3]);
                            square.getOverlay().setStroke(color);
                            for (int r = 0; r < 9; r++) {
                                for (int c = 0; c < 9; c++) {
                                    if (ui.getBoard().getSquare(r, c).getOverlay().getStroke().equals(Color.BLACK)) {
                                        ui.getBoard().getSquare(r, c).getOverlay().setStrokeWidth(1);
                                    } else {
                                        ui.getBoard().getSquare(r, c).getOverlay().setStrokeWidth(3);
                                    }
                                }
                            }
                            int numFinished = 0;
                            for (int num = 0; num < 9; num++) {
                                int numPresent = 0;
                                for (int r = 0; r < 9; r++) {
                                    for (int c = 0; c < 9; c++) {
                                        if (ui.getBoard().getSquare(r, c).getAnswer().getValue() == (num + 1)
                                                && ui.getBoard().getSquare(r, c).getAnswer().getValue()
                                                == ui.getControl().getSolnBoard()[r][c]) {
                                            numPresent++;
                                            numFinished++;
                                        }
                                    }
                                }
                                if (numPresent == 9) {
                                    ui.getMenu().getNumber(num).setDisable(true);
                                }
                            }
                            // We've completed the game.
                            if (numFinished == 81) {
                                Platform.runLater(() -> {
                                    ParallelTransition animation = new ParallelTransition();
                                    for (int r = 0; r < 9; r++) {
                                        SequentialTransition transRow = new SequentialTransition();
                                        for (int c = 0; c < 9; c++) {
                                            Square transSquare = ui.getBoard().getSquare(r, c);
                                            RotateTransition rotate = new RotateTransition(new Duration(100), transSquare);
                                            rotate.setFromAngle(0);
                                            rotate.setToAngle(360);
                                            transRow.getChildren().add(rotate);
                                        }
                                        animation.getChildren().add(transRow);
                                    }
                                    animation.playFromStart();
                                });
                            }
                        }
                    }
                    if (instruct.isLast()) {
                        Platform.runLater(() -> ui.getControl().ready());
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
