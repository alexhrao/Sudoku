package main.java.server.networking;

import java.io.Serializable;


public class Data implements Serializable {
    //TODO: Make this an inner class of SudokuPacket
    private double[] ansColor;
    private double[] overColor;
    private int ans = 0;
    private int[] notes;
    private int[] posn;
    private boolean selected;

    public Data(double[] ansColor, double[] overColor, int ans, int[] notes, int[] posn, boolean selected) {
        this.ansColor = ansColor;
        this.overColor = overColor;
        this.ans = ans;
        this.notes = notes;
        this.posn = posn;
        this.selected = selected;
    }

    public double[] getAnsColor() {
        return ansColor;
    }

    public int getAns() {
        return ans;
    }

    public int[] getNotes() {
        return notes;
    }

    public int[] getPosn() {
        return posn;
    }

    public boolean isSelected() {
        return selected;
    }

    public double[] getOverColor() {
        return overColor;
    }


}
