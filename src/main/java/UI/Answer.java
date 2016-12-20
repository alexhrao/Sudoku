package main.java.UI;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Answer extends Text {

    public Answer() {
        this.clear();
        this.setFont(new Font(81));
    }
    public Answer(int ans) {
        this();
        this.setAnswer(ans);
    }

    public void setAnswer(int ans) {
        this.setText(Integer.toString(ans));
        this.setVisible(true);
    }

    public void clear() {
        this.setText("");
        this.setVisible(false);
    }
}
