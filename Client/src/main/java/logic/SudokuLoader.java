package main.java.logic;

import javafx.application.Preloader;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SudokuLoader extends Preloader {
    private Stage stage;
    private Button statusButton;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/icon.png"));
        ImageView splash = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        splash.setFitHeight(700);
        splash.setFitWidth(700);
        statusButton = new Button("Play!");
        statusButton.setOnAction(e -> primaryStage.close());
        primaryStage.getIcons().add(splash.getImage());
        statusButton.setTranslateY(310);
        statusButton.setMinWidth(73.5);
        statusButton.setMinHeight(73);
        statusButton.setTextFill(Color.RED);
        statusButton.setFont(new Font(20));
        statusButton.setBackground(new Background(new BackgroundFill(Color.rgb(248, 196, 115), new CornerRadii(10), null)));
        StackPane splashPane = new StackPane(splash, statusButton);
        Scene scene = new Scene(splashPane);
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
