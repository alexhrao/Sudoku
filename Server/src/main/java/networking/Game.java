package main.java.networking;

/**
 * This class will be responsible for keeping games separate. It will do (most) of what the server currently does now!
 */
public class Game extends ThreadGroup {

    public Game(String name) {
        super(name);
    }

    public Game(ThreadGroup group, String name) {
        super(group, name);
    }
}
