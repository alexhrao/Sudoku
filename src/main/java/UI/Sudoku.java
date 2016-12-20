package main.java.UI;

import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Sudoku extends Application {
    private GameUI ui = new GameUI();
    private Controller control;
    @Override
    public void start(Stage primaryStage) throws Exception {
        control = new Controller();
        setup();
        Scene game = new Scene(ui, 1400, 1300);
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
                control.setPlay(!control.getPlay());
            }
        });
        for (int i = 0; i < 9; i++) {
            final int num = i;
            menu.getNumber(i).setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (control.getNote()) {
                        control.getLastClicked().getNotes().toggle(num + 1);
                    } else {
                        control.getLastClicked().getNotes().clear();
                        control.getLastClicked().getAnswer().setAnswer(num + 1);
                    }
                }
            });
        }

        final Board board = ui.getBoard();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                final Square sq = board.getSquare(r, c);
                board.getSquare(r, c).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        for (int r = 0; r < 9; r++) {
                            for (int c = 0; c < 9; c++) {
                                board.getSquare(r, c).getOverlay().setStroke(Color.BLACK);
                                board.getSquare(r, c).getOverlay().setStrokeWidth(1);
                            }
                        }
                        control.setLastClicked(sq);
                        sq.getOverlay().setStroke(control.getColor());
                        sq.getOverlay().setStrokeWidth(2);
                    }
                });
            }
        }

    }
}
