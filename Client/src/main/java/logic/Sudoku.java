package main.java.logic;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
    private Scene game;

    /**
     * The start method, overriding the one in Application.
     * @param primaryStage The Primary Window for this application.
     * @throws Exception Throws this if setup fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.gatherInformation();
        ui = new GameUI(control);
        game = new Scene(ui);
        this.setup();
        player = new SudokuListener(control.getServerHost(), control.getServerPort(), ui);
        player.start();
        control.setLoader(loader);
        loader.getStage().showAndWait();
        primaryStage.setTitle("Sudoku Online");
        primaryStage.setScene(game);
        primaryStage.getIcons().add(icon);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((WindowEvent n) -> {
            try {
                SudokuSender sender = new SudokuSender(control);
                (new Thread(sender)).start();
                player.getClient().close();
            } catch (IOException | NullPointerException ignore) {
            }
        });
        game.getStylesheets().add(getClass().getResource("/Loader.css").toExternalForm());
        primaryStage.show();
    }

    /**
     * The main method, kept for legacy systems.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    //TODO: Add SinglePlayer Option
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
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        GridPane controls = new GridPane();
        GridPane colorPane = new GridPane();
        Image infoScreen = null;
        Stage infoStage = new Stage();

        try {
            BufferedImage bufferedImage = ImageIO.read(getClass().getResource("/icon.png"));
            icon = SwingFXUtils.toFXImage(bufferedImage, null);
            bufferedImage = ImageIO.read(getClass().getResource("/SudokuInfoScreen.png"));
            infoScreen = SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView background = new ImageView(infoScreen);
        background.setFitWidth(screenHeight / 1.3);
        background.setFitHeight(screenHeight / 1.3);
        // Controls
        TextField name = new TextField("Your Name");
        name.setMaxWidth(screenHeight / 4.7);
        name.setFont(new Font(screenHeight / 65));

        Text playerAskColor = new Text("Player Color:  ");
        playerAskColor.setFont(new Font(screenHeight / 65));
        playerAskColor.setFill(Color.BROWN);
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        colorPicker.setMaxWidth(screenHeight / 5);
        colorPane.add(playerAskColor, 0, 0);
        colorPane.add(colorPicker, 1, 0);

        TextField spaces = new TextField("30");
        spaces.setFont(new Font(screenHeight / 65));
        spaces.setMaxWidth(screenHeight / 9.2926);
        GridPane spacesPane = new GridPane();
        Text manySpaces = new Text("How many spaces?");
        manySpaces.setFont(new Font(screenHeight / 65));
        manySpaces.setFill(Color.BROWN);
        spacesPane.add(manySpaces, 0, 0);
        spacesPane.add(spaces, 1, 0);
        spacesPane.setPadding(new Insets(5, 0, 5, 0));
        spacesPane.setVisible(false);

        GridPane uploadPane = new GridPane();
        final Text file = new Text("Choose File...");
        file.setUnderline(true);
        file.setFont(new Font(screenHeight / 65));
        file.setFill(Color.BROWN);
        Button uploadBoard = new Button("Upload...");
        final String[] stringBoard = new String[9];
        uploadBoard.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Sudoku Board files", "*.sdbd", "*.csv", "*.txt"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
            fileChooser.setTitle("Select board to upload:");
            File chosen;
            chosen = fileChooser.showOpenDialog(infoStage);
            if (chosen != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(chosen))) {
                    for (int count = 0; count < 9; count++) {
                        stringBoard[count] = reader.readLine() + ",";
                    }
                    file.setText(chosen.getName());
                } catch (IOException | NullPointerException f) {
                    stringBoard[0] = null;
                    file.setText("Choose File...");
                }
            } else {
                stringBoard[0] = null;
                file.setText("Choose File...");
            }
        });

        uploadBoard.setTextFill(Color.BROWN);
        uploadBoard.setFont(new Font(screenHeight / 65));
        uploadPane.add(uploadBoard, 0, 0);
        uploadPane.add(file, 1, 0);

        TextField sHost = new TextField("localhost");
        sHost.setFont(new Font(screenHeight / 65));
        sHost.setMinWidth(screenHeight / 4.911);
        TextField sPort = new TextField("60000");
        sPort.setFont(new Font(screenHeight / 65));
        sPort.setMinWidth(screenHeight / 4.911);

        GridPane advanced = new GridPane();
        Text serverHostPrompt = new Text("Host:  ");
        Text serverPortPrompt = new Text("Port:  ");
        serverHostPrompt.setFill(Color.BROWN);
        serverHostPrompt.setFont(new Font(screenHeight / 65));
        serverPortPrompt.setFill(Color.BROWN);
        serverPortPrompt.setFont(new Font(screenHeight / 65));
        advanced.add(serverHostPrompt, 0, 0);
        advanced.add(sHost, 1, 0);
        advanced.add(serverPortPrompt, 0, 1);
        advanced.add(sPort, 1, 1);
        advanced.setPadding(new Insets(5, 0, 5, 0));
        advanced.setVisible(false);

        Button showAdvanced = new Button("Show Advanced Options:");
        showAdvanced.setFont(new Font(screenHeight / 65));
        showAdvanced.setTextFill(Color.BROWN);
        showAdvanced.setOnAction(e -> {
            if (advanced.isVisible()) {
                showAdvanced.setText("Show Advanced Options");
                advanced.setVisible(false);
            } else {
                showAdvanced.setText("Hide Advanced Options");
                advanced.setVisible(true);
            }
        });
        showAdvanced.setMinWidth(screenHeight / 3.9963);

        Button done = new Button("Done");
        done.setTextFill(Color.BROWN);
        done.setFont(new Font(screenHeight / 65));
        done.setMinWidth(screenHeight / 8.32);
        Button cancel = new Button("Cancel");
        cancel.setTextFill(Color.BROWN);
        cancel.setFont(new Font(screenHeight / 65));
        cancel.setOnAction(e -> System.exit(0));
        cancel.setMinWidth(screenHeight / 8.32);
        HBox buttons = new HBox(10, done, cancel);

        controls.add(name, 0, 1);
        controls.add(colorPane, 0, 2, 2, 1);
        //controls.add(spacesPane, 0, 3);
        controls.add(uploadPane, 0, 3, 2, 1);
        controls.add(buttons, 0, 4);
        controls.add(showAdvanced, 0, 5, 1, 1);
        controls.add(advanced, 0, 6, 2, 2);
        controls.setTranslateY(screenHeight / 1.92);
        //controls.setTranslateX(screenHeight / 200);
        controls.setTranslateX(-screenHeight / 4.7);
        controls.setMaxWidth(screenHeight / 3);

        StackPane infoPane = new StackPane(background, controls);
        Scene infoScene = new Scene(infoPane);
        infoScene.setFill(Color.TRANSPARENT);
        infoScene.getStylesheets().add(getClass().getResource("/Loader.css").toExternalForm());
        infoStage.setScene(infoScene);
        infoStage.setResizable(false);
        infoStage.getIcons().add(icon);
        done.setOnAction(e -> infoStage.close());
        infoStage.setTitle("Player Information");
        infoStage.setOnCloseRequest((WindowEvent e) -> System.exit(0));
        infoStage.initStyle(StageStyle.TRANSPARENT);
        infoStage.showAndWait();

        loader = new SudokuLoader();
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

        boolean isValid = true;
        do {
            if (playerName.isEmpty()) {
                name.setText("Cannot be blank!");
                isValid = false;
            }
            if (playerColor.equals(Color.BLACK) || playerColor.equals(Color.WHITE)
                    || playerColor.equals(Color.DARKRED) || playerColor.equals(Color.GREEN)
                    || playerColor.equals(Color.TRANSPARENT)) {
                colorPicker.setPromptText("Invalid color chosen!");
                playerAskColor.setFill(Color.RED);
                isValid = false;
            }
            if (!isValid) {
                infoStage.showAndWait();
            }
        } while (!isValid);
        loader.start(infoStage);
        control = new Controller(playerName, playerColor, serverName, serverPort);
        if (stringBoard[0] == null) {
            control.setSpaces(numSpaces);
        } else {
            control.setInput(stringBoard);
        }
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
        menu.getNewGame().setOnAction((ActionEvent e) -> {
            SudokuSender sender = new SudokuSender(control, "isNew");
            (new Thread(sender)).start();
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
                ui.getBoard().getSquare(control.getLastClicked().getRow(), control.getLastClicked().getCol()).clear();
                ui.getBoard().getSquare(control.getLastClicked().getRow(), control.getLastClicked().getCol()).getAnswer().setValue(num + 1);
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
                    sq.getOverlay().setStrokeWidth(3);
                    if (!(sq.getAnswer().getVisible()
                            && !sq.getAnswer().getFill().equals(Color.DARKRED))) {
                        if (sq.getAnswer().getValue() == ans) {
                            sq.getAnswer().setFill(control.getColor());
                        } else {
                            sq.getAnswer().setFill(Color.DARKRED);
                        }
                    }
                    for (int num = 0; num < 9; num++) {
                        int numPresent = 0;
                        for (int r = 0; r < 9; r++) {
                            for (int c = 0; c < 9; c++) {
                                if (board.getSquare(r, c).getAnswer().getValue() == (num + 1)) {
                                    numPresent++;
                                }
                            }
                        }
                        if (numPresent == 9) {
                            menu.getNumber(num).setDisable(true);
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
        game.setOnKeyTyped((KeyEvent n) -> {
            try {
                int num = Integer.parseInt(n.getCharacter()) - 1;
                if (!ui.getMenu().getNumber(num).isDisabled()) {
                    ui.getMenu().getNumber(num).fire();
                }
            } catch (NumberFormatException ignored) {
            }
        });
    }
}
