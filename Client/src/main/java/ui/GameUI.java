package main.java.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.logic.Controller;
import main.java.logic.SudokuLoader;
import main.java.networking.SudokuSender;

/**
 * GameUI is a BorderPane that handles all the UI elements. It also has a few convenience methods. The controller is
 * synchronized.
 */
public class GameUI extends BorderPane {
    private final Board board = new Board();
    private final ButtonMenu menu = new ButtonMenu();
    private final InfoMenu info;
    private final Controller control;
    private final Chat chat;
    private final Chatter chatter;
    @SuppressWarnings("FieldCanBeLocal")
    private final SudokuLoader splash;

    /**
     * Creates a complete visual representation of the current Sudoku Game.
     * @param control The current controller for this game.
     * @param splash The Splash Screen (Deprecated!)
     */
    public GameUI(Controller control, SudokuLoader splash) {
        this.control = control;
        this.splash = splash;
        GridPane squares = new GridPane();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Rectangle box = new Rectangle(255, 255, Color.rgb(0, 0, 0, 0.0));
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(2.5);
                squares.add(box, c, r);
            }
        }
        info = new InfoMenu(this);
        StackPane center = new StackPane(squares, board);
        chat = new Chat(control);
        chatter = new Chatter();
        chatter.getSender().setOnAction(e -> {
            chat.thisPlayerChat(chatter.getChatter().getText());
            chatter.getSender().setDisable(true);
            SudokuSender sender = new SudokuSender(control, control.getColor(), chatter.getChatter().getText());
            Thread tClient = new Thread(sender);
            tClient.start();
            chatter.getChatter().clear();
        });
        chatter.getChatter().setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                chatter.getSender().fire();
            }
        });
        chatter.getChatter().setOnKeyReleased(e -> {
            if (chatter.getChatter().getText().isEmpty()) {
                chatter.getSender().setDisable(true);
            } else {
                chatter.getSender().setDisable(false);
            }
        });
        Rectangle overlay = new Rectangle(chat.getChatWidth() + 6, 770, Color.color(0, 0, 0, 0));
        overlay.setStroke(Color.color(0, 0, 0, 1));
        overlay.setStrokeWidth(4);
        ScrollPane chatScroll = new ScrollPane(chat);
        chatScroll.setFitToWidth(true);
        chatScroll.setMinWidth(300);
        StackPane chatPane = new StackPane(overlay, chatScroll);
        this.setTop(info);
        this.setCenter(center);
        this.setLeft(menu);
        this.setRight(chatPane);
        this.setBottom(chatter);
    }

    /**
     * Gets the Board that represents the current game.
     * @return A Board of this game (FX).
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the current ButtonMenu that is positioned on the left.
     * @return The current ButtonMenu.
     */
    public ButtonMenu getMenu() {
        return menu;
    }

    /**
     * Gets the current ToolBar that is positioned on the top.
     * @return A ToolBar that has all the player information.
     */
    public InfoMenu getInfo() {
        return info;
    }

    /**
     * A synchronized method that gets the Solution Board. This is a convenience method that retrieves the Solution from
     * the controller.
     * @return A double-layered integer array that represents the solution.
     */
    public synchronized int[][] getSolnBoard() {
        return this.control.getSolnBoard();
    }

    /**
     * A synchronized method that sets the solution board. This is a convenience method that sets the Solution board of
     * the controller.
     * @param solnBoard A double-layered int array that represents the solution.
     */
    public synchronized void setSolnBoard(int[][] solnBoard) {
        this.control.setSolnBoard(solnBoard);
    }

    /**
     * Gets the controller.
     * @return The current game's controller.
     */
    public Controller getControl() {
        return this.control;
    }

    /**
     * Gets the Side Chat positioned on the right side.
     * @return The current game's chat log.
     */
    public Chat getChat() {
        return chat;
    }

// --Commented out by Inspection START (1/2/2017 3:44 PM):
//    /**
//     * Gets the User's chat sender, positioned on the bottom side.
//     * @return The current game's chat sender.
//     */
//    public Chatter getChatter() {
//        return chatter;
//    }
// --Commented out by Inspection STOP (1/2/2017 3:44 PM)
}
