package main.java.server.logic;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.server.generator.Generator;
import main.java.server.generator.Grid;
import main.java.server.generator.Solver;
import main.java.server.networking.SudokuClient;
import main.java.server.networking.SudokuServer;
import main.java.server.ui.Board;
import main.java.server.ui.ButtonMenu;
import main.java.server.ui.GameUI;
import main.java.server.ui.Square;
import java.io.IOException;

public class Sudoku extends Application{
    private GameUI ui;
    private Controller control;
    private Generator generator = new Generator();
    private int[][] gameBoard;
    private int[][] solnBoard;
    private SudokuServer server;
    private Stage primaryStage;
    private String playerName;
    private Color playerColor;
    private String hostName;
    private int hostPort;
    private String serverName;
    private int serverPort;
    private int numSpaces;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.gatherInformation();
        this.primaryStage = primaryStage;
        control = new Controller(playerName, playerColor, serverName, hostName, serverPort, hostPort);
        ui = new GameUI(playerName, playerColor, control);
        server = new SudokuServer(control.getServerHost(), control.getServerPort(), ui);
        Thread tServer = new Thread(server);
        tServer.start();
        this.setup();
        ui.getBoard().populate(gameBoard);
        Scene game = new Scene(ui, 1175, 825);
        primaryStage.setTitle("Sudoku");
        primaryStage.setScene(game);
        primaryStage.getIcons().add(
                new Image("File:./src/main/resources/icon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void gatherInformation() {
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
        GridPane advanced = new GridPane();
        GridPane colorPane = new GridPane();
        ImageView background = new ImageView(new Image("File:./src/main/resources/icon.png"));
        // Controls
        TextField name = new TextField("Your Name");
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField spaces = new TextField("30");
        spaces.setMaxWidth(40);
        GridPane spacesPane = new GridPane();
        spacesPane.add(new Text("How many spaces?"), 0, 0);
        spacesPane.add(spaces, 1, 0);
        TextField serverHost = new TextField("localhost");
        TextField sPort = new TextField("60000");
        sPort.setMaxWidth(75);
        GridPane serverPane = new GridPane();
        serverPane.add(new Text("Server Host & Port:"), 0, 0);
        serverPane.add(serverHost, 1, 0);
        serverPane.add(sPort, 2, 0);
        TextField clientHost = new TextField("localhost");
        TextField clientPort = new TextField("60000");
        clientPort.setMaxWidth(75);
        advanced.setVisible(false);

        Button done = new Button("Done");
        Button showAdvanced = new Button("Show Advanced Options:");
        showAdvanced.setOnAction(e -> {
            if (advanced.isVisible()) {
                showAdvanced.setText("Show Advanced Options:");
                advanced.setVisible(false);
            } else {
                showAdvanced.setText("Hide Advanced Options:");
                advanced.setVisible(true);
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> System.exit(0));

        advanced.add(new Text("Client Host & Port:"), 0, 1);
        advanced.add(clientHost, 1, 1);
        advanced.add(clientPort, 2, 1);
        colorPane.add(new Text("Pick your color:"), 0, 0);
        colorPane.add(colorPicker, 1, 0);
        controls.add(name, 0, 1);
        controls.add(colorPane, 0, 2);
        controls.add(spacesPane, 0, 3);
        controls.add(serverPane, 0, 4, 3, 1);
        controls.add(showAdvanced, 0, 5, 3, 1);
        controls.add(advanced, 0, 6, 3, 1);
        controls.add(done, 0, 7);
        controls.add(cancel, 1, 7);

        StackPane infoPane = new StackPane(background, controls);
        Stage infoStage = new Stage();
        Scene infoScene = new Scene(infoPane);
        infoScene.setFill(null);
        infoStage.setScene(infoScene);
        infoStage.setResizable(false);

        infoStage.getIcons().add(new Image("File:./src/main/resources/icon.png"));
        done.setOnAction(e -> infoStage.close());
        infoStage.setTitle("Player Information");
        infoStage.showAndWait();
        playerName = name.getText();
        playerColor = colorPicker.getValue();
        numSpaces = Integer.parseInt(spaces.getText());
        serverName = serverHost.getText();
        try {
            serverPort = Integer.parseInt(sPort.getText());
        } catch (Exception e) {
            serverPort = 0;
        }
        hostName = clientHost.getText();
        try {
            hostPort = Integer.parseInt(clientPort.getText());
        } catch (Exception e) {
            hostPort = 0;
        }
    }

    private void setup() {
        Grid game = generator.generate(numSpaces);
        gameBoard = Grid.to(game);
        Solver solver = new Solver();
        solver.solve(game);
        solnBoard = Grid.to(game);
        ui.setSolnBoard(solnBoard);
        ui.getMenu().getSend().setOnAction((ActionEvent e) -> {
            SudokuClient clientStart;
            clientStart = new SudokuClient(control.getClientHost(), control.getClientPort(), gameBoard, solnBoard);
            Thread tClientStart = new Thread(clientStart);
            tClientStart.start();
            SudokuClient playerInfo = new SudokuClient(control.getClientHost(), control.getClientPort(), control.getName(), control.getColor(), true);
            Thread tplayerInfo = new Thread(playerInfo);
            tplayerInfo.start();
        });
        ButtonMenu menu = ui.getMenu();
        menu.getNote().setOnAction((ActionEvent e) -> {
            control.setNote(!control.getNote());
            if (control.getNote()) {
                menu.getNote().setText("Answer");
            } else {
                menu.getNote().setText("Note");
            }
        });
        menu.getClear().setOnAction((ActionEvent e) -> control.getLastClicked().clear());
        menu.getPause().setOnAction((ActionEvent e) -> {
            if (control.getPlay()) {
                menu.getPause().setText("Pause");
                ui.getInfo().setPause(false);
            } else {
                menu.getPause().setText("Play");
                ui.getInfo().setPause(true);
            }
            control.setPlay(!control.getPlay());
        });
        menu.getHint().setOnAction((ActionEvent e) -> {
            Square sq = control.getLastClicked();
            sq.clear();
            int r = sq.getRow();
            int c = sq.getCol();
            sq.getAnswer().setValue(ui.getSolnBoard()[r][c]);
            sq.getAnswer().setFill(Color.GREEN);
            menu.disable();
            SudokuClient client = new SudokuClient(control.getClientHost(), control.getClientPort(), sq);
            Thread tClient = new Thread(client);
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
                        if (gameBoard[r][c] == (num + 1)) {
                            numPresent++;
                        }
                    }
                }
                if (numPresent == 9) {
                    menu.getNumber(num).setDisable(true);
                }
                if (control.getNote()) {
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
                SudokuClient client;
                client = new SudokuClient(control.getClientHost(), control.getClientPort(), control.getLastClicked());
                Thread tClient = new Thread(client);
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
                    SudokuClient client;
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
                        client = new SudokuClient(control.getClientHost(), control.getClientPort(), sq);
                    } else {
                        client = new SudokuClient(control.getClientHost(), control.getClientPort(), first, sq);
                    }
                    Thread tClient = new Thread(client);
                    tClient.start();
                });
            }
        }
        primaryStage.setOnCloseRequest((WindowEvent n) -> {
            try {
                server.getServer().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public GameUI getUi() {
        return ui;
    }

    public Controller getControl() {
        return control;
    }

    public int[][] getSolnBoard() {
        return solnBoard;
    }
}
