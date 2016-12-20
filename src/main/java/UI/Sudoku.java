package main.java.UI;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Sudoku extends Application {
    private GameUI ui = new GameUI();
    @Override
    public void start(Stage primaryStage) throws Exception {
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
}
