package main.java.logic;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.java.networking.SudokuSender;
import main.java.networking.SudokuListener;
import main.java.ui.Board;
import main.java.ui.ButtonMenu;
import main.java.ui.GameUI;
import main.java.ui.Square;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * This is the entry point of the Sudoku game. It collects starting information from the player, creates the game, and
 * manages the actual logic used to play the game.
 */
public class Sudoku extends Application{
    private GameUI ui;
    private Controller control;
    private SudokuListener player;
    private SudokuLoader loader;
    private Image icon;

    /**
     * The start method, overriding the one in Application.
     * @param primaryStage The Primary Window for this application.
     * @throws Exception Throws this if setup fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.gatherInformation();
        ui = new GameUI(control);
        this.setup();
        player = new SudokuListener(control.getServerHost(), control.getServerPort(), ui);
        player.start();
        Scene game = new Scene(ui);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(game);
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((WindowEvent n) -> {
            try {
                SudokuSender sender = new SudokuSender(control);
                (new Thread(sender)).start();
                player.getClient().close();
            } catch (IOException | NullPointerException e) {
            }
        });
        primaryStage.show();
    }

    /**
     * The main method, kept for legacy systems.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }


    private void gatherInformation() throws Exception {
        /* Regardless of server set up, we need to capture:
            * Player Name
            * Player Color
            * Number of free spaces.
            * Server Host & Port
            * Client Host & Port (Advanced) -> if not given, use any available one (0).
            *
           We will need:
            * A Background image
            * A grid of the uicontrols.
         */
        // Structure:
        GridPane controls = new GridPane();
        GridPane colorPane = new GridPane();
        Image selection = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/icon.png"));
            icon = SwingFXUtils.toFXImage(bufferedImage, null);
            bufferedImage = ImageIO.read(getClass().getResource("/selection.png"));
            selection = SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView background = new ImageView(icon);
        background.setFitWidth(700);
        background.setFitHeight(700);
        // Controls
        TextField name = new TextField("Your Name");
        name.setMaxWidth(150);
        name.setFont(new Font(16));
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField spaces = new TextField("30");
        spaces.setFont(new Font(16));
        spaces.setMaxWidth(40);
        GridPane spacesPane = new GridPane();
        Text manySpaces = new Text("How many spaces?");
        manySpaces.setFont(new Font(18));
        manySpaces.setFill(Color.BROWN);
        spacesPane.add(manySpaces, 0, 0);
        spacesPane.add(spaces, 1, 0);
        spacesPane.setPadding(new Insets(5, 0, 5, 0));
        TextField sHost = new TextField("localhost");
        sHost.setFont(new Font(16));
        TextField sPort = new TextField("60000");
        sPort.setFont(new Font(16));
        sPort.setMaxWidth(75);
        GridPane advanced = new GridPane();
        Text serverHostPort = new Text("Server Host & Port");
        serverHostPort.setFill(Color.BROWN);
        serverHostPort.setFont(new Font(16));
        advanced.add(serverHostPort, 0, 0);
        advanced.add(sHost, 1, 0);
        advanced.add(sPort, 2, 0);
        advanced.setPadding(new Insets(5, 0, 5, 0));
        advanced.setVisible(false);

        Button done = new Button("Done");
        done.setTextFill(Color.BROWN);
        Button showAdvanced = new Button("Show Advanced Options:");
        showAdvanced.setFont(new Font(16));
        showAdvanced.setOnAction(e -> {
            if (advanced.isVisible()) {
                showAdvanced.setText("Show Advanced Options");
                advanced.setVisible(false);
            } else {
                showAdvanced.setText("Hide Advanced Options");
                advanced.setVisible(true);
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.BROWN);
        cancel.setOnAction(e -> System.exit(0));
        HBox buttons = new HBox(10, done, cancel);

        Text playerAskColor = new Text("Player Color:  ");
        playerAskColor.setFont(new Font(18));
        playerAskColor.setFill(Color.BROWN);
        colorPane.add(playerAskColor, 0, 0);
        colorPane.add(colorPicker, 1, 0);
        controls.add(name, 0, 1);
        controls.add(colorPane, 0, 2);
        controls.add(spacesPane, 0, 3);
        controls.add(new HBox(10, showAdvanced), 0, 4, 1, 1);
        controls.add(advanced, 0, 5, 2, 1);
        controls.add(buttons, 0, 6);

/*        name.setTranslateY(-111);
        name.setTranslateX(-270);
        name.setMaxWidth(150);
        colorPane.setTranslateY(290);
        colorPane.setTranslateX(10);
        spacesPane.setTranslateY(300);
        spacesPane.setTranslateX(10);
        advanced.setTranslateX(10);*/
        controls.setTranslateY(480);
        controls.setTranslateX(10);
        StackPane infoPane = new StackPane(background, controls);
        //StackPane.setMargin(controls, new Insets(100, 10, 100, 10));
        Stage infoStage = new Stage();
        Scene infoScene = new Scene(infoPane);
        infoScene.setFill(null);
        infoStage.setScene(infoScene);
        // infoStage.setResizable(true);

        infoStage.getIcons().add(icon);
        done.setOnAction(e -> infoStage.close());
        infoStage.setTitle("Player Information");
        infoStage.setOnCloseRequest((WindowEvent e) -> System.exit(0));
        infoStage.initStyle(StageStyle.TRANSPARENT);
        infoStage.showAndWait();
        loader = new SudokuLoader();
        loader.start(infoStage);
        String playerName = name.getText();
        Color playerColor = colorPicker.getValue();
        int numSpaces = Integer.parseInt(spaces.getText());
        String serverName = sHost.getText();
        int serverPort;
        try {
            serverPort = Integer.parseInt(sPort.getText());
        } catch (Exception e) {
            serverPort = 0;
        }

        control = new Controller(playerName, playerColor, serverName, serverPort);
        control.setSpaces(numSpaces);
    }

    private void setup() {
        ui.getMenu().getSend().setVisible(false);
        ButtonMenu menu = ui.getMenu();
        menu.getNote().setOnAction((ActionEvent e) -> {
            control.setNote(!control.isNote());
            if (control.isNote()) {
                menu.getNote().setText("Answer");
            } else {
                menu.getNote().setText("Note");
            }
        });
        menu.getClear().setOnAction((ActionEvent e) -> {
            control.getLastClicked().clear();
            SudokuSender sender = new SudokuSender(control, control.getLastClicked());
            (new Thread(sender)).start();
        });
        menu.getPause().setOnAction((ActionEvent e) -> {
            if (control.isPlay()) {
                menu.getPause().setText("Pause");
                ui.getInfo().setPause(false);
            } else {
                menu.getPause().setText("Play");
                ui.getInfo().setPause(true);
            }
            control.setPlay(!control.isPlay());
        });
        menu.getHint().setOnAction((ActionEvent e) -> {
            Square sq = control.getLastClicked();
            sq.clear();
            int r = sq.getRow();
            int c = sq.getCol();
            sq.getAnswer().setValue(ui.getSolnBoard()[r][c]);
            sq.getAnswer().setFill(Color.GREEN);
            menu.disable();
            SudokuSender sender = new SudokuSender(control, sq);
            Thread tClient = new Thread(sender);
            tClient.start();
        });
        for (int i = 0; i < 9; i++) {
            final int num = i;
            menu.getNumber(i).setOnAction((ActionEvent e) -> {
                if (control.getLastClicked().getAnswer().getVisible()
                        && !control.getLastClicked().getAnswer().getFill().equals(Color.DARKRED)) {
                    return;
                }
                int numPresent = 0;
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (control.getBoard()[r][c] == (num + 1)) {
                            numPresent++;
                        }
                    }
                }
                if (numPresent == 9) {
                    menu.getNumber(num).setDisable(true);
                }
                if (control.isNote()) {
                    control.getLastClicked().getAnswer().clear();
                    control.getLastClicked().getNotes().toggle(num);
                } else {
                    for (int r = 0; r < 9; r++) {
                        int c = control.getLastClicked().getCol();
                        if (!ui.getBoard().getSquare(r, c).getAnswer().getVisible()) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num);
                        }
                    }
                    for (int c = 0; c < 9; c++) {
                        int r = control.getLastClicked().getRow();
                        if (!ui.getBoard().getSquare(r, c).getAnswer().getVisible()) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num);
                        }
                    }

                    for (int r = (int) (3 * Math.floor(control.getLastClicked().getRow() / 3)) - 1;
                         r < 3 * Math.ceil(control.getLastClicked().getRow() / 3) - 1; r++) {
                        for (int c = (int) (3 * Math.floor(control.getLastClicked().getCol() / 3)) - 1;
                             c < 3 * Math.ceil(control.getLastClicked().getCol() / 3) - 1; c++) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num);
                        }
                    }
                    control.getLastClicked().getNotes().clear();
                    control.getLastClicked().getAnswer().setValue(num);
                    int r = control.getLastClicked().getRow();
                    int c = control.getLastClicked().getCol();
                    if ((num + 1) == ui.getSolnBoard()[r][c]) {
                        menu.disable();
                        control.getLastClicked().getAnswer().setFill(control.getColor());
                    } else {
                        menu.enable();
                        control.getLastClicked().getAnswer().setFill(Color.DARKRED);
                    }
                }
                SudokuSender sender = new SudokuSender(control, control.getLastClicked());
                Thread tClient = new Thread(sender);
                tClient.start();
            });
        }

        final Board board = ui.getBoard();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                final Square sq = board.getSquare(row, col);
                board.getSquare(row, col).setOnMouseClicked((MouseEvent e) -> {
                    int ans = ui.getSolnBoard()[sq.getRow()][sq.getCol()];
                    for (int r = 0; r < 9; r++) {
                        for (int c = 0; c < 9; c++) {
                            if (board.getSquare(r, c).getOverlay().getStroke().equals(control.getColor())) {
                                board.getSquare(r, c).getOverlay().setStroke(Color.BLACK);
                            }
                            board.getSquare(r, c).getOverlay().setStrokeWidth(1);
                            board.getSquare(r, c).setSelected(false);
                        }
                    }
                    if (control.getLastClicked() != null) {
                        control.getLastClicked().setSelected(false);
                    }
                    Square first = control.getLastClicked();
                    control.setLastClicked(sq);
                    SudokuSender sender;
                    sq.setSelected(true);
                    if (sq.getAnswer().getVisible() && !sq.getAnswer().getFill().equals(Color.DARKRED)) {
                        ui.getMenu().disable();
                    } else {
                        ui.getMenu().enable();
                    }
                    sq.getOverlay().setStroke(control.getColor());
                    if (!(sq.getAnswer().getVisible()
                            && !sq.getAnswer().getFill().equals(Color.DARKRED))) {
                        if (sq.getAnswer().getValue() == ans) {
                            sq.getAnswer().setFill(control.getColor());
                        } else {
                            sq.getAnswer().setFill(Color.DARKRED);
                        }
                    }
                    if (first == null) {
                        sender = new SudokuSender(control, sq);
                    } else {
                        sender = new SudokuSender(control, first, sq);
                    }
                    Thread tClient = new Thread(sender);
                    tClient.start();
                });
            }
        }
    }
}
