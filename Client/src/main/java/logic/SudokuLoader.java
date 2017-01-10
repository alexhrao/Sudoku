package main.java.logic;

import javafx.application.Preloader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SudokuLoader extends Preloader implements Runnable {
    private Button statusButton;
    private Stage stage;

    @Override
    public void run() {
        this.stage.showAndWait();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        this.stage = primaryStage;
        BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/icon.png"));
        ImageView splash = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        splash.setFitHeight(screenHeight / 1.3);
        splash.setFitWidth(screenHeight / 1.3);
        statusButton = new Button("Wait");
        statusButton.setDisable(true);
        statusButton.setOnAction(e -> primaryStage.close());
        primaryStage.getIcons().add(splash.getImage());
        statusButton.setTranslateY(screenHeight / 2.91);
        statusButton.setMinWidth(screenHeight / 12.1);
        statusButton.setMinHeight(screenHeight / 12.09);
        statusButton.setTextFill(Color.RED);
        statusButton.setFont(new Font(screenHeight / 52));
        statusButton.setBackground(new Background(new BackgroundFill(Color.rgb(248, 196, 115), new CornerRadii(screenHeight / 104), null)));
        StackPane splashPane = new StackPane(splash, statusButton);
        Scene loadScene = new Scene(splashPane);
        loadScene.getStylesheets().add(getClass().getResource("/Loader.css").toExternalForm());
        loadScene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(loadScene);
    }

    public void ready() {
        this.statusButton.setText("Play");
        this.statusButton.setDisable(false);
    }

    public Stage getStage() {
        return this.stage;
    }

    public Button getStatusButton() {
        return this.statusButton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
