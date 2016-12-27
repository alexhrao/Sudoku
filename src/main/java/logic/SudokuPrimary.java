package main.java.logic;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
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

public class SudokuPrimary extends Application{
    private GameUI ui;
    private Controller control;
    private Generator generator = new Generator();
    private int[][] gameBoard;
    private int[][] solnBoard;
    private SudokuServer server;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //TODO: Add Primary Launcher!
        this.primaryStage = primaryStage;
        control = new Controller("Alex Rao", Color.RED, "localhost", "localhost", 60000, 60001);
        ui = new GameUI("Alex Rao", Color.RED, control);
        server = new SudokuServer(control.getServerHost(), control.getServerPort(), ui);
        Thread tServer = new Thread(server);
        tServer.start();
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
                int numPresent = 0;
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (gameBoard[r][c] == (num + 1)) {
                            numPresent++;
                        }
                    }
                }
                //TODO: clear boxes when we answer!
                /*if (numPresent == 9) {
                    menu.getNumber(num).setDisable(true);
                }*/
                int r = control.getLastClicked().getRow();
                int c = control.getLastClicked().getCol();
                if (control.getLastClicked().getAnswer().getVisible()
                        && !control.getLastClicked().getAnswer().getFill().equals(Color.DARKRED)) {
                    return;
                }
                if (control.getNote()) {
                    control.getLastClicked().getAnswer().clear();
                    control.getLastClicked().getNotes().toggle(num + 1);
                } else {
                    control.getLastClicked().getNotes().clear();
                    control.getLastClicked().getAnswer().setValue(num + 1);
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
