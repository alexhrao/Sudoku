package main.java.UI;

import javafx.scene.text.Text;

/**
 * Created by alexh on 12/19/2016.
 */
public class Answer extends Text {

    public Answer() {
        this.clear();
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
