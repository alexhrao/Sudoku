package main.java.logic;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.generator.Generator;
import main.java.generator.Grid;
import main.java.generator.Solver;
import main.java.networking.SudokuClient;
import main.java.networking.SudokuServer;
import main.java.ui.Board;
import main.java.ui.ButtonMenu;
import main.java.ui.GameUI;
import main.java.ui.Square;
import java.io.IOException;
import java.util.Optional;

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
                new Image("File:./src/main/java/resources/icon.png"));
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
        ImageView background = new ImageView("File:./src/main/java/resources/icon.png");
        GridPane controls = new GridPane();
        GridPane advanced = new GridPane();
        GridPane colorPane = new GridPane();
        TextField name = new TextField("Your Name");
        ColorPicker colorPicker = new ColorPicker(Color.RED);
        TextField spaces = new TextField("Number of Spaces");
        TextField serverHost = new TextField("Server Host");
        TextField sPort = new TextField("Server Port");
        TextField clientHost = new TextField("Client Host");
        TextField clientPort = new TextField("Client Port");

        advanced.add(clientHost, 0, 0);
        advanced.add(clientPort, 1, 0);
        colorPane.add(new Text("Pick your color:"), 0, 0);
        colorPane.add(colorPicker, 1, 0);
        controls.add(new Text("Please enter your information:"), 0, 0);
        controls.add(name, 0, 1);
        controls.add(colorPane, 0, 2);
        controls.add(spaces, 0, 3);
        controls.add(serverHost, 0, 4);
        controls.add(sPort, 1, 4);
        controls.add(advanced, 0, 5, 2, 2);

        StackPane infoPane = new StackPane(background, controls);
        Stage infoStage = new Stage();
        Scene infoScene = new Scene(infoPane);
        infoStage.setScene(infoScene);
        infoStage.getIcons().add(background.getImage());
        infoStage.showAndWait();

        
        playerName = name.getText();
        playerColor = colorPicker.getValue();
        serverName = serverHost.getText();
        serverPort = Integer.parseInt(sPort.getText());
        hostName = clientHost.getText();
        hostPort = Integer.parseInt(clientPort.getText());


        /*Stage tdStage;
        TextInputDialog tdName = new TextInputDialog("Player 1");
        tdName.setHeaderText("Enter your name.");
        tdName.setTitle("Name");
        tdStage = (Stage) tdName.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        Optional<String> name = tdName.showAndWait();
        if (name.isPresent()) {
            playerName = name.get();
        } else {
            playerName = "Player 1";
        }

        playerColor = new ColorPicker(Color.RED);
        Button done = new Button("Done");
        Text explain = new Text("Please select a color.");
        VBox info = new VBox(20, playerColor, explain, done);
        info.setAlignment(Pos.CENTER);
        Scene color = new Scene(info);
        Stage colorStage = new Stage();
        done.setOnAction(e -> {
            colorStage.close();
        });
        colorStage.setScene(color);
        colorStage.setTitle("Pick your color");
        colorStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        colorStage.showAndWait();

        TextInputDialog tdHost = new TextInputDialog("localhost");
        tdHost.setHeaderText("What is the host address?");
        tdHost.setTitle("Host address");
        tdStage = (Stage) tdHost.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        Optional<String> host = tdHost.showAndWait();
        if (host.isPresent()) {
            hostName = host.get();
        } else {
            hostName = "localhost";
        }

        TextInputDialog tdHostPort = new TextInputDialog("60000");
        tdHostPort.setHeaderText("What is the host port?");
        tdHostPort.setTitle("Host Port");
        tdStage = (Stage) tdHostPort.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        host = tdHostPort.showAndWait();
        if (host.isPresent()) {
            hostPort = Integer.parseInt(host.get());
        } else {
            hostPort = 60000;
        }

        TextInputDialog tdServer = new TextInputDialog("localhost");
        tdHost.setHeaderText("What is the server address?");
        tdHost.setTitle("Server address");
        tdStage = (Stage) tdServer.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        Optional<String> server = tdHost.showAndWait();
        if (server.isPresent()) {
            serverName = host.get();
        } else {
            serverName = "localhost";
        }

        TextInputDialog tdServerPort = new TextInputDialog("60001");
        tdServerPort.setHeaderText("What is the server port?");
        tdServer.setTitle("Server Port");
        tdStage = (Stage) tdServerPort.getDialogPane().getScene().getWindow();
        tdStage.getIcons().add(new Image("File:./src/main/java/resources/icon.png"));
        server = tdServerPort.showAndWait();
        if (server.isPresent()) {
            serverPort = Integer.parseInt(server.get());
        } else {
            serverPort = 60001;
        }*/
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
        ui.setSolnBoard(solnBoard);
        control.setBoard(gameBoard);
        control.setSoln(solnBoard);
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
                    control.getLastClicked().getNotes().toggle(num + 1);
                } else {
                    for (int r = 0; r < 9; r++) {
                        int c = control.getLastClicked().getCol();
                        if (!ui.getBoard().getSquare(r, c).getAnswer().getVisible()) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num + 1);
                        }
                    }
                    for (int c = 0; c < 9; c++) {
                        int r = control.getLastClicked().getRow();
                        if (!ui.getBoard().getSquare(r, c).getAnswer().getVisible()) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num + 1);
                        }
                    }

                    for (int r = (int) (3 * Math.floor(control.getLastClicked().getRow() / 3)) - 1;
                         r < 3 * Math.ceil(control.getLastClicked().getRow() / 3) - 1; r++) {
                        for (int c = (int) (3 * Math.floor(control.getLastClicked().getCol() / 3)) - 1;
                             c < 3 * Math.ceil(control.getLastClicked().getCol() / 3) - 1; c++) {
                            ui.getBoard().getSquare(r, c).getNotes().hide(num + 1);
                        }
                    }
                    control.getLastClicked().getNotes().clear();
                    control.getLastClicked().getAnswer().setValue(num + 1);
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
