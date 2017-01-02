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
        this.stage = primaryStage;
        BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/icon.png"));
        ImageView splash = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        splash.setFitHeight(825);
        splash.setFitWidth(825);
        statusButton = new Button("Wait");
        statusButton.setDisable(true);
        statusButton.setOnAction(e -> primaryStage.close());
        primaryStage.getIcons().add(splash.getImage());
        statusButton.setTranslateY(366);
        statusButton.setMinWidth(86);
        statusButton.setMinHeight(85);
        statusButton.setTextFill(Color.RED);
        statusButton.setFont(new Font(20));
        statusButton.setBackground(new Background(new BackgroundFill(Color.rgb(248, 196, 115), new CornerRadii(10), null)));
        StackPane splashPane = new StackPane(splash, statusButton);
        Scene scene = new Scene(splashPane);
        primaryStage.setScene(scene);
        this.ready();
    }

    private void ready() {
        this.statusButton.setText("Play!");
        this.statusButton.setDisable(false);
    }

    public Stage getStage() {
        return this.stage;
    }

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    public Button getStatusButton() {
//        return this.statusButton;
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)

    public static void main(String[] args) {
        launch(args);
    }
}
