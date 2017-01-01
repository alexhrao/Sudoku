package main.java.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.logic.Controller;
import main.java.networking.Chat;
import main.java.networking.SudokuSender;

/**
 * GameUI is a BorderPane that handles all the UI elements. It also has a few convenience methods. The controller is
 * synchronized.
 */
public class GameUI extends BorderPane {
    private Board board = new Board();
    private ButtonMenu menu = new ButtonMenu();
    private InfoMenu info;
    private volatile Controller control;
    private Chat chat;
    private Chatter chatter;

    /**
     * Creates a complete visual representation of the current Sudoku Game.
     * @param control The current controller for this game.
     */
    public GameUI(Controller control) {
        this.control = control;
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
            SudokuSender sender = new SudokuSender(control, control.getColor(), chatter.getChatter().getText());
            Thread tClient = new Thread(sender);
            tClient.start();
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

    /**
     * Gets the User's chat sender, positioned on the bottom side.
     * @return The current game's chat sender.
     */
    public Chatter getChatter() {
        return chatter;
    }
}
