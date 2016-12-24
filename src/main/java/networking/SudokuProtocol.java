package main.java.networking;


import javafx.scene.paint.Color;
import main.java.ui.Square;
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

    public SudokuProtocol(Data... data) {
        this.data = data;
        this.packet = new SudokuPacket(data);
    }

    public SudokuProtocol(String name, Color color) {
        this.packet = new SudokuPacket(name, color);
        this.data = packet.getData();
    }

    public SudokuPacket getPacket() {
        return packet;
    }

    public Data[] getData() {
        return this.data;
    }
}
