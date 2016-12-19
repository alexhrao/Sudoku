package main.java;

import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * Created by alexh on 12/19/2016.
 */
public class Board extends GridPane {

    private Square[][] squares = new Square[9][9];
    public Board() {
        this.setGridLinesVisible(true);
        ColumnConstraints colCons = new ColumnConstraints();
        colCons.setPercentWidth(100 / 9);
        RowConstraints rowCons = new RowConstraints();
        rowCons.setPercentHeight(100 / 9);
        for(int r = 0; r < 9; r++) {
            this.getRowConstraints().add(rowCons);
            this.getColumnConstraints().add(colCons);
            for (int c = 0; c < 9; c++) {

                squares[r][c] = new Square();
                squares[r][c].show(1);
                this.add(squares[r][c], c, r);
            }
        }
    }

    public Square getSquare(int row, int col) {
        return squares[row][col];
    }
}