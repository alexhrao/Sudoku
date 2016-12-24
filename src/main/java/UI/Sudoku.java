package main.java.ui;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.generator.Generator;
import main.java.generator.Grid;
import main.java.generator.Solver;
import main.java.logic.Controller;
import main.java.networking.SudokuClient;
import main.java.networking.SudokuServer;

import java.util.Optional;

public class Sudoku extends Application {
    private GameUI ui = new GameUI();
    private Controller control;
    private Generator generator = new Generator();
    private int[][] gameBoard;
    private int[][] solnBoard;
    private SudokuClient client;
    private Thread tClient;

    @Override
    public void start(Stage primaryStage) throws Exception {
        control = new Controller();
        setup();
        ui.getBoard().populate(gameBoard);
        Scene game = new Scene(ui, 870, 800);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(game);
        primaryStage.getIcons().add(
                new Image("File:./src/main/java/resources/icon.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setup() {
        TextInputDialog td = new TextInputDialog("30");
        td.setHeaderText("How many free spaces would you like?");
        td.setTitle("Difficulty");
        Stage tdStage = (Stage) td.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        Optional<String> strSpaces = td.showAndWait();
        int numSpaces = 30;
        if (strSpaces.isPresent()) {
            numSpaces = Integer.parseInt(strSpaces.get());
        }
        Grid game = generator.generate(numSpaces);
        gameBoard = Grid.to(game);
        Solver solver = new Solver();
        solver.solve(game);
        solnBoard = Grid.to(game);
        control.setBoard(gameBoard);
        control.setSoln(solnBoard);

        ButtonMenu menu = ui.getMenu();
        menu.getNote().setOnAction(e -> {
            control.setNote(!control.getNote());
            if (control.getNote()) {
                menu.getNote().setText("Answer");
            } else {
                menu.getNote().setText("Note");
            }
        });
        menu.getClear().setOnAction(e -> control.getLastClicked().clear());
        menu.getPause().setOnAction(e -> {
            if (control.getPlay()) {
                menu.getPause().setText("Pause");
                ui.getInfo().setPause(false);
            } else {
                menu.getPause().setText("Play");
                ui.getInfo().setPause(true);
            }
            control.setPlay(!control.getPlay());
        });
        menu.getHint().setOnAction(e -> {
            Square sq = control.getLastClicked();
            sq.clear();
            int r = sq.getRow();
            int c = sq.getCol();
            sq.getAnswer().setValue(control.getSoln()[r][c]);
            sq.getOverlay().setStroke(Color.GREEN);
            sq.getAnswer().setFill(Color.GREEN);
            menu.disable();
        });
        for (int i = 0; i < 9; i++) {
            final int num = i;
            menu.getNumber(i).setOnAction(e -> {
                int numPresent = 0;
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (gameBoard[r][c] == (num + 1)) {
                            numPresent++;
                        }
                    }
                }
                //TODO: clear boxes when we answer!
                /*if (numPresent == 9) {
                    menu.getNumber(num).setDisable(true);
                }*/
                int r = control.getLastClicked().getRow();
                int c = control.getLastClicked().getCol();
                if (control.getLastClicked().getAnswer().getVisible()
                        && ((control.getLastClicked().getAnswer().getFill() == Color.BLACK)
                        || (control.getLastClicked().getAnswer().getFill() == Color.GREEN))) {
                    return;
                }
                if (control.getNote()) {
                    control.getLastClicked().getAnswer().clear();
                    control.getLastClicked().getNotes().toggle(num + 1);
                } else {
                    control.getLastClicked().getNotes().clear();
                    control.getLastClicked().getAnswer().setValue(num + 1);
                    if ((num + 1) == solnBoard[r][c]) {
                        menu.disable();
                        control.getLastClicked().getAnswer().setFill(control.getColor());
                    } else {
                        menu.enable();
                        control.getLastClicked().getAnswer().setFill(Color.DARKRED);
                    }
                }
            });
        }

        final Board board = ui.getBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final Square sq = board.getSquare(row, col);
                final int ans = solnBoard[row][col];
                board.getSquare(row, col).setOnMouseClicked(e -> {
                    for (int r = 0; r < 9; r++) {
                        for (int c = 0; c < 9; c++) {
                            if (board.getSquare(r, c).getOverlay().getStroke() == control.getColor())
                                board.getSquare(r, c).getOverlay().setStroke(Color.BLACK);
                            board.getSquare(r, c).getOverlay().setStrokeWidth(1);
                        }
                    }
                    if (control.getLastClicked() != null) {
                        control.getLastClicked().setSelected(false);
                    }
                    Square first = control.getLastClicked();
                    if (first == null) {
                        client = new SudokuClient(sq);
                    } else {
                        client = new SudokuClient(first, sq);
                    }
                    control.setLastClicked(sq);
                    tClient = new Thread(client);
                    tClient.start();

                    sq.setSelected(true);
                    if (sq.getAnswer().getVisible() && sq.getAnswer().getValue() == ans) {
                        ui.getMenu().disable();
                    } else {
                        ui.getMenu().enable();
                    }
                    sq.getOverlay().setStroke(control.getColor());
                    if (control.getLastClicked().getAnswer().getVisible()
                            && ((control.getLastClicked().getAnswer().getFill() == Color.BLACK)
                            || (control.getLastClicked().getAnswer().getFill() == Color.GREEN))) {
                        return;
                    }
                    if (sq.getAnswer().getValue() == ans) {
                        sq.getAnswer().setFill(control.getColor());
                    } else {
                        sq.getAnswer().setFill(Color.DARKRED);
                    }
                });
            }
        }
    }
}
