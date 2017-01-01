package main.java.networking;

import javafx.scene.paint.Color;

import main.java.generator.Generator;
import main.java.generator.Grid;
import main.java.generator.Solver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class is responsible for actually talking with the client. It relays information to and from itself and other
 * clients.
 */
public class SudokuServerThread extends Thread {
    private Socket client;
    private volatile SudokuServer server;
    private SudokuPacket packet;
    private volatile boolean isGoing = true;
    private volatile int id;

    /**
     * Creates a thread with the server and the socket.
     * @param socket The accepted socket.
     * @param server The parent server.
     */
    public SudokuServerThread(Socket socket, SudokuServer server) {
        super("SudokuServerThread");
        this.client = socket;
        this.server = server;
    }

    /**
     * Talks with the player and relays information to other threads, if need be.
     */
    @Override
    public synchronized void run() {
        /* If it's data, we'll need to send this to the rest of the threads in threads. As such, just set the data for
         each of these threads, then interrupt that thread! On interrupt, that thread should send the now-obtained data
         back to it's connected client.
         If it is NOT data (it's a new client connecting), Then it should be perpetually waiting (like I said above).
         If it's the first player, then isBoard will be true. We should store that board in each thread.

         OK, so here's how I think it should work:
         No matter who you are, the first thing you send is your information and your boards (original, solution). THEN,
         You'll get a reply that has the boards you should use, and all the player information you'd need. We should
         also send the current state of the board (who has clicked where, etc). This should override anything on the
         client.
         So, if you're the first player, your response should just be your information only AND your own boards.
         If you're NOT the first player, your response will be your information AND any other player information, and
         the current board. This means we should probably have a way to completely serialize a board (with player
         locations, etc.)

         If nobody clicks anything, this is how it stays. The live threads are players.
         AS SOON as someone clicks on something, however, this changes. A New connection is added.
         As such, if the isBoard and isPlayer are both false, then this is a modification, NOT a new player.
         So, for each thread in threads, we should first test if it is alive. If it's alive, then:
            Use the setPacket() method, and feed our packet.
            Interrupt that thread.
        Now, each thread should have a try-catch method - if the thread is interrupted, it should SEND out it's data.
        After it's done this for each live thread, it should return, thus killing itself.

        So, if the input is a board and player:
            Check if stored board / player has been set (if there has been a first player; have a boolean for this?)
            If not:
                Set this board, and somehow store this board at the server level.
                Set this player information at the server level.
                respond with this serialized information.

        Change of plans (slightly). We should JUST send our player information; if a board has not already been created,
        create one and send it back, setting the board as well. Otherwise, send back the stored board.

        Basically, it's this. If we get a new player, then we should ALWAYS respond with a board and player combination.
        If the board hasn't already been created, then we should create it. Otherwise, reply back with the existing
        board and solution. Otherwise, respond back with the already.
                */
        try (ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream())) {
            out.flush();
            try (ObjectInputStream reader = new ObjectInputStream(client.getInputStream())) {
                SudokuPacket instruct;
                instruct = (SudokuPacket) reader.readObject();
                if (instruct.isStarter()) {
                    if (server.isFirstPlayer()) {
                        Generator generator = new Generator();
                        Solver solver = new Solver();
                        int[][] board = Grid.to(generator.generate(instruct.getSpaces()));
                        Grid soln = generator.generate(instruct.getSpaces());
                        solver.solve(soln);
                        int[][] solnBoard = Grid.to(soln);
                        server.setBoards(board, solnBoard);
                    }
                    System.out.println("Connected to player " + instruct.getName());
                    id = server.addPlayer(instruct.getName(), Color.color(instruct.getColor()[0], instruct.getColor()[1], instruct.getColor()[2], instruct.getColor()[3]));
                    SudokuPacket response = new SudokuPacket(server.getBoard(), server.getSoln());
                    out.writeObject(response);
                    // send all the player information:
                    SudokuPacket thisPlayer = new SudokuPacket(instruct.getName(), server.getPlayerColor().get(id - 1), id);
                    out.writeObject(thisPlayer);
                    for (int i = 0; i < server.getPlayerName().size() - 1; i++) {
                        if (server.getPlayerName().get(i) != null) {
                            SudokuPacket player = new SudokuPacket(server.getPlayerName().get(i), server.getPlayerColor().get(i), server.getPlayerId().get(i));
                            out.writeObject(player);
                        }
                    }
                    for (SudokuPacket packet : server.getPackets()) {
                        out.writeObject(packet);
                    }
                    for (SudokuServerThread thread : server.getThreads()) {
                        if (thread.isAlive()) {
                            SudokuPacket player = new SudokuPacket(instruct.getName(), Color.color(instruct.getColor()[0], instruct.getColor()[1], instruct.getColor()[2], instruct.getColor()[3]),
                                    id);
                            thread.setPacket(player);
                            thread.interrupt();
                        }
                    }
                    server.getThreads().add(this);
                    // Wait to be interrupted!
                    while (isGoing) {
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            out.writeObject(this.packet);
                        }
                    }
                } else {
                    server.addPacket(instruct);
                    if (instruct.isRemove()) {
                        throw new SocketException();
                    }
                    for (SudokuServerThread thread : server.getThreads()) {
                        if (thread.isAlive()) {
                            thread.setPacket(instruct);
                            thread.interrupt();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            try {
                server.getThreads().remove(this);
            } catch (Exception n) {
                e.printStackTrace();
            }
            SudokuPacket instruct = new SudokuPacket(this.id);
            server.addPacket(instruct);
            for (SudokuServerThread thread : server.getThreads()) {
                if (thread.isAlive()) {
                    thread.setPacket(instruct);
                    thread.interrupt();
                }
            }
            server.removePlayer(this.id);
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void setPacket(SudokuPacket packet) {
        this.packet = packet;
    }

    public synchronized void halt() {
        this.isGoing = false;
    }
}
