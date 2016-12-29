package main.java.server.networking;


import javafx.scene.paint.Color;
import main.java.server.ui.Square;
import java.io.Serializable;

public class SudokuProtocol implements Serializable {

    private SudokuPacket packet;
    private Data[] data;

    public SudokuProtocol(SudokuPacket packet) {
        this.packet = packet;
        this.data = packet.getData();
    }

    public SudokuProtocol(Square... squares) {
        this.packet = new SudokuPacket(squares);
        this.data = packet.getData();
    }

    public SudokuProtocol(int[][] board, int[][] solnBoard) {
        this.packet = new SudokuPacket(board, solnBoard);
        this.data = packet.getData();
    }

    public SudokuProtocol(Data... data) {
        this.data = data;
        this.packet = new SudokuPacket(data);
    }

    public SudokuProtocol(String name, Color color, boolean isReturn) {
        this.packet = new SudokuPacket(name, color, isReturn);
        this.data = packet.getData();
    }

    public SudokuProtocol(String message, Color color) {
        this.packet = new SudokuPacket(message, color);
        this.data = packet.getData();
    }

    public SudokuPacket getPacket() {
        return packet;
    }

    public Data[] getData() {
        return this.data;
    }
}
