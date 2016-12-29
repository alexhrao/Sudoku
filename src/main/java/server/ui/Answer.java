package main.java.server.ui;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Answer extends Text implements Comparable<Answer> {
    private boolean isVisible = false;
    private int value = 0;
    public Answer() {
        this.clear();
        this.setFont(new Font(50));
        this.setFill(Color.WHITE);
    }

    public void setValue(int ans) {
        if (ans > 9 || ans < 1) {
            this.clear();
            this.setFill(Color.WHITE);
        } else {
            this.setText(Integer.toString(ans));
            this.setVisible(true);
            isVisible = true;
            value = ans;
            this.setFill(Color.BLACK);
        }
    }

    public int getValue() {
        return value;
    }

    public void clear() {
        this.setText("");
        this.setVisible(false);
        isVisible = false;
        value = 0;
    }

    public boolean getVisible() {
        return isVisible;
    }

    @Override
    public int compareTo(Answer answer) {
        return answer.getValue() - this.getValue();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Answer)) {
            throw new IllegalArgumentException("Valid variable of class Answer not given!");
        } else {
            return ((Answer) object).getValue() == this.getValue();
        }
    }
}
