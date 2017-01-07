package main.java.ui;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import main.java.logic.Controller;
import main.java.logic.SudokuLoader;
import main.java.networking.SudokuSender;

import java.awt.*;

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
    private final ScrollPane chatScroll;

    /**
     * Creates a complete visual representation of the current Sudoku Game.
     * @param control The current controller for this game.
     */
    public GameUI(Controller control) {
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        this.control = control;
        GridPane squares = new GridPane();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                Rectangle box = new Rectangle(screenHeight / 4.0784, screenHeight / 4.0784, Color.rgb(0, 0, 0, 0.0));
                box.setStroke(Color.BLACK);
                box.setStrokeWidth(2.5);
                squares.add(box, c, r);
            }
        }
        squares.setDisable(true);
        info = new InfoMenu(this, screenHeight / 41.6);
        StackPane center = new StackPane(board, squares);
        chat = new Chat(screenHeight / 3.4667, control);
        chatter = new Chatter(screenHeight / 1.3867);
        chatter.getSender().setOnAction(e -> {
            chat.thisPlayerChat(chatter.getChatter().getText());
            chatter.getSender().setDisable(true);
            SudokuSender sender = new SudokuSender(control, control.getColor(), chatter.getChatter().getText());
            this.scrollToBottom();
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
        Rectangle overlay = new Rectangle(chat.getChatWidth() + 6, screenHeight / 2.805, Color.color(0, 0, 0, 0));
        overlay.setStroke(Color.color(0, 0, 0, 1));
        overlay.setStrokeWidth(4);
        chatScroll = new ScrollPane(chat);
        chatScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        chatScroll.setFitToWidth(true);
        chatScroll.setVmin(0);
        chatScroll.setVmax(1);
        chatScroll.setMinWidth(screenHeight / 7.2);
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

    public void scrollToBottom() {
        this.chatScroll.setVvalue(1);
    }
}
