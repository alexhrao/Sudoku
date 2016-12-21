package main.java.UI;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.java.generator.Generator;
import main.java.generator.Grid;
import main.java.generator.Solver;

import java.util.Optional;

public class Sudoku extends Application {
    private GameUI ui = new GameUI();
    private Controller control;
    private Generator generator = new Generator();
    private int[][] gameBoard;
    private int[][] solnBoard;

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

    public void setup() {
        TextInputDialog td = new TextInputDialog("30");
        td.setHeaderText("How many free spaces would you like?");
        td.setTitle("Difficulty");
        Stage stage = (Stage) td.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
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
        menu.getNote().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                control.setNote(!control.getNote());
            }
        });
        menu.getClear().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                control.getLastClicked().clear();
            }
        });
        menu.getPause().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (control.getPlay()) {
                    menu.getPause().setText("Pause");
                    ui.getInfo().setPause(false);
                } else {
                    menu.getPause().setText("Play");
                    ui.getInfo().setPause(true);
                }
                control.setPlay(!control.getPlay());
            }
        });

        menu.getHint().setOnAction(n -> {
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
            menu.getNumber(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    int r = control.getLastClicked().getRow();
                    int c = control.getLastClicked().getCol();
                    if (control.getLastClicked().getAnswer().getVisible()
                            && (control.getLastClicked().getAnswer().getFill() == Color.BLACK)) {
                        return;
                    }
                    if (control.getNote()) {
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
                }
            });
        }

        final Board board = ui.getBoard();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                final Square sq = board.getSquare(r, c);
                final int ans = solnBoard[r][c];
                board.getSquare(r, c).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        for (int r = 0; r < 9; r++) {
                            for (int c = 0; c < 9; c++) {
                                if (board.getSquare(r, c).getOverlay().getStroke() == control.getColor())
                                board.getSquare(r, c).getOverlay().setStroke(Color.BLACK);
                                board.getSquare(r, c).getOverlay().setStrokeWidth(1);
                            }
                        }
                        control.setLastClicked(sq);
                        if (sq.getAnswer().getVisible() && sq.getAnswer().getValue() == ans) {
                            ui.getMenu().disable();
                        } else {
                            ui.getMenu().enable();
                        }
                        sq.getOverlay().setStroke(control.getColor());
                        if (control.getLastClicked().getAnswer().getVisible()
                                && (control.getLastClicked().getAnswer().getFill() == Color.BLACK)) {
                            return;
                        }
                        if (sq.getAnswer().getValue() == ans) {
                            sq.getAnswer().setFill(control.getColor());
                        } else {
                            sq.getAnswer().setFill(Color.DARKRED);
                        }
                    }
                });
            }
        }
    }
}
