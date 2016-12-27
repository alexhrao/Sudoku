package main.java.networking;

import javafx.scene.paint.Color;
import main.java.ui.Square;

import java.io.Serializable;

/**
 * Created by alexh on 12/22/2016.
 */
public class SudokuPacket implements Serializable {
    private Data[] data;
    private String name;
    private double[] color = new double[4];
    private int[][] board;
    private int[][] solnBoard;
    private boolean isBoard = false;
    private boolean isPlayer = false;
    private boolean isReturn = false;
    private boolean isMessage = false;
    private String message;

    public SudokuPacket(String name, Color color, boolean isReturn) {
        this.name = name;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
        this.isPlayer = true;
        this.data = null;
        this.isReturn = isReturn;
    }

    public SudokuPacket(int[][] board, int[][] solnBoard) {
        this.board = board;
        this.solnBoard = solnBoard;
        this.isBoard = true;
        this.data = null;
    }

    public SudokuPacket(String message) {
        this(message, Color.BLACK);
    }
    public SudokuPacket(String message, Color color) {
        this.message = message;
        this.isMessage = true;
        this.data = null;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
    }

    public SudokuPacket(Square... squares) {
        this.data = new Data[squares.length];
        for (int s = 0; s < squares.length; s++) {
            Square sq = squares[s];
            double[] ansColor = new double[4];
            ansColor[0] = ((Color) sq.getAnswer().getFill()).getRed();
            ansColor[1] = ((Color) sq.getAnswer().getFill()).getGreen();
            ansColor[2] = ((Color) sq.getAnswer().getFill()).getBlue();
            ansColor[3] = ((Color) sq.getAnswer().getFill()).getOpacity();
            double[] overColor = new double[4];
            overColor[0] = ((Color) sq.getOverlay().getStroke()).getRed();
            overColor[1] = ((Color) sq.getOverlay().getStroke()).getGreen();
            overColor[2] = ((Color) sq.getOverlay().getStroke()).getBlue();
            overColor[3] = ((Color) sq.getOverlay().getStroke()).getOpacity();
            int ans = sq.getAnswer().getValue();
            int count = 0;
            boolean[] visibility = sq.getNotes().getVisibility();
            for (int i = 0; i < 9; i++) {
                if (visibility[i]) {
                    count++;
                }
            }
            int[] notes = new int[count];
            int k = 0;
            for (int i = 0; i < 9; i++) {
                if (visibility[i]) {
                    notes[k] = i;
                    k++;
                }
            }
            int[] posn = new int[2];
            posn[0] = sq.getRow();
            posn[1] = sq.getCol();
            boolean selected = sq.isSelected();
            this.data[s] = new Data(ansColor, overColor, ans, notes, posn, selected);
        }
    }

    public SudokuPacket(Data...data) {
        this.data = data;
    }

    public Data[] getData() {
        return this.data;
    }

    public boolean isBoard() {
        return isBoard;
    }

    public boolean isPlayer() {
        return isPlayer;
    }

    public int[][] getBoard() {
        return board;
    }

    public int[][] getSolnBoard() {
        return solnBoard;
    }

    public double[] getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public boolean isReturn() {
        return this.isReturn;
    }

    public boolean isMessage() {
        return this.isMessage;
    }

    public String getMessage() {
        return this.message;
    }
}
