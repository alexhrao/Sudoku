package main.java.networking;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.ui.GameUI;
import main.java.ui.Square;

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
                if (instruct.isBoard()) {
                    int[][] board = instruct.getBoard();
                    int[][] solnBoard = instruct.getSolnBoard();
                    ui.setSolnBoard(solnBoard);
                    ui.getControl().setSoln(solnBoard);
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
                    double[] color = instruct.getColor();
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
                    if (instruct.isReturn()) {
                        SudokuClient playerInfo = new SudokuClient(ui.getControl().getClientHost(), ui.getControl().getClientPort(), ui.getControl().getName(), ui.getControl().getColor(), false);
                        Thread tplayerInfo = new Thread(playerInfo);
                        tplayerInfo.start();
                    }
                } else if (instruct.isMessage()) {
                    String message = instruct.getMessage();
                    double[] color = instruct.getColor();
                    class AddChat implements Runnable {
                        @Override
                        public void run() {
                            ui.getChat().thatPlayerChat(message, Color.color(color[0], color[1], color[2], color[3]));
                        }
                    }
                    Platform.runLater(new AddChat());
                } else {
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
                                    square.getNotes().show(n + 1);
                                } else {
                                    square.getNotes().hide(n + 1);
                                }
                            }
                        }
                        square.getOverlay().setStroke(Color.color(overColor[0], overColor[1], overColor[2], overColor[3]));
                    }
                }
            }
        } catch(EOFException e){
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
}
