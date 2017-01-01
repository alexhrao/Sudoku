package main.java.networking;

import javafx.scene.paint.Color;
import main.java.ui.Square;

import java.io.Serializable;

/**
 * This class is responsible for trading data between the server and clients. It is serializable, and represents the
 * basic data that is sent over the socket. ONLY SudokuPackets may be sent.
 */
public class SudokuPacket implements Serializable {
    private Data[] data;
    private String name;
    private double[] color = new double[4];
    private int[][] board;
    private int[][] solnBoard;
    private int spaces;
    private boolean isBoard = false;
    private boolean isPlayer = false;
    private boolean isMessage = false;
    private boolean isStarter = false;
    private boolean isRemove = false;
    private String message;
    private int id;

    /**
     * This constructure makes a packet for a new player.
     * @param name The player name.
     * @param color The player color.
     * @param isPlayer An identifier for if it is a player.
     */
    public SudokuPacket(String name, Color color, boolean isPlayer) {
        this.name = name;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
        this.isPlayer = true;
        this.data = null;
    }

    /**
     * This constructor makes a packet for a removed player.
     * @param name The name of the player.
     * @param color The color of the player.
     * @param id The id of the player.
     */
    public SudokuPacket(String name, Color color, int id) {
        this(name, color, true);
        this.id = id;
    }

    /**
     * This constructur creates the board and the beginning volley.
     * @param board The board.
     * @param solnBoard The solution.
     * @param name The player name.
     * @param color The player color.
     */
    @Deprecated
    public SudokuPacket(int[][] board, int[][] solnBoard, String name, Color color) {
        this.name = name;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
        this.board = board;
        this.solnBoard = solnBoard;
        this.isBoard = true;
        this.isPlayer = true;
    }

    /**
     * This constructor creates the first sent packet.
     * @param spaces Number of board spaces.
     * @param name Player name.
     * @param color Player color.
     */
    public SudokuPacket(int spaces, String name, Color color) {
        this.name = name;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
        this.spaces = spaces;
        this.isStarter = true;
    }

    /**
     * Creates a packet for the board and solution board.
     * @param board The board.
     * @param solnBoard The solution.
     */
    public SudokuPacket(int[][] board, int[][] solnBoard) {
        this.board = board;
        this.solnBoard = solnBoard;
        this.isBoard = true;
        this.data = null;
    }

    /**
     * Creates a packet for a message.
     * @param message The message.
     * @param color The color.
     * @param id The id of the player.
     * @param isMessage If it's a message.
     */
    public SudokuPacket(String message, Color color, int id, boolean isMessage) {
        this.message = message;
        this.isMessage = true;
        this.id = id;
        this.data = null;
        this.color[0] = color.getRed();
        this.color[1] = color.getGreen();
        this.color[2] = color.getBlue();
        this.color[3] = color.getOpacity();
    }

    /**
     * This creates a packet for squares.
     * @param squares Modified squares.
     */
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

    /**
     * Creates a packet for removing a player.
     * @param id the id of the player.
     */
    public SudokuPacket(int id) {
        this.id = id;
        this.isRemove = true;
    }

    /**
     * This creates a packet from the given data.
     * @param data The Data.
     */
    @Deprecated
    public SudokuPacket(Data...data) {
        this.data = data;
    }

    /**
     * Gets the square data, if any.
     * @return Square data.
     */
    public Data[] getData() {
        return this.data;
    }

    /**
     * Tells if the packet contains a board.
     * @return If this contains a board.
     */
    public boolean isBoard() {
        return isBoard;
    }

    /**
     * Tells if the packet contains player information.
     * @return If this contains player information.
     */
    public boolean isPlayer() {
        return isPlayer;
    }

    /**
     * Gets the board.
     * @return A double-layered int array, 9x9.
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Gets the solution.
     * @return A double-layered int array, 9x9.
     */
    public int[][] getSolnBoard() {
        return solnBoard;
    }

    /**
     * Gets the color, as a double array. Use Color.color to reconstruct.
     * @return A 1x4 double array.
     */
    public double[] getColor() {
        return color;
    }

    /**
     * Gets the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Tells if this packet contains message information.
     * @return If this package contains message information.
     */
    public boolean isMessage() {
        return this.isMessage;
    }

    /**
     * Gets the actual message.
     * @return The content of the message.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Tells if this packet has starting information.
     * @return If this packet is a starter packet.
     */
    public boolean isStarter() {
        return this.isStarter;
    }

    /**
     * Gets the number of requested spaces.
     * @return The number of blank squares.
     */
    public int getSpaces() {
        return this.spaces;
    }

    /**
     * Returns the player id.
     * @return The player id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Tells if this packet removes a player.
     * @return If this packet removes a player.
     */
    public boolean isRemove() {
        return this.isRemove;
    }

    /**
     * A convenience class that stores one full square.
     */
    public class Data implements Serializable {
        private double[] ansColor;
        private double[] overColor;
        private int ans = 0;
        private int[] notes;
        private int[] posn;
        private boolean selected;

        /**
         * Constructs Data from a square.
         * @param ansColor The answer color.
         * @param overColor The overlay color.
         * @param ans The answer.
         * @param notes The notes.
         * @param posn The position ([Row, Col]).
         * @param selected If this square is currently selected.
         */
        public Data(double[] ansColor, double[] overColor, int ans, int[] notes, int[] posn, boolean selected) {
            this.ansColor = ansColor;
            this.overColor = overColor;
            this.ans = ans;
            this.notes = notes;
            this.posn = posn;
            this.selected = selected;
        }

        /**
         * Gets the color of the Answer itself.
         * @return The 1x4 double for reconstructing the Answer Color.
         */
        public double[] getAnsColor() {
            return ansColor;
        }

        /**
         * Gets the answer value.
         * @return The answer value.
         */
        public int getAns() {
            return ans;
        }

        /**
         * Gets the notes.
         * @return Returns the int array of showing notes.
         */
        public int[] getNotes() {
            return notes;
        }

        /**
         * Gets the position.
         * @return Returns an int[Row, Col].
         */
        public int[] getPosn() {
            return posn;
        }

        /**
         * Tells if this square is selected or not.
         * @return If the square is selected.
         */
        public boolean isSelected() {
            return selected;
        }

        /**
         * Returns the overlay color.
         * @return Gives a 1x4 double; use Color.color to reconstruct the original color.
         */
        public double[] getOverColor() {
            return overColor;
        }
    }
}