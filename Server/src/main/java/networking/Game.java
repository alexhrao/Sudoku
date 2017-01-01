package main.java.networking;

/**
 * This class will be responsible for keeping games separate. It will do (most) of what the server currently does now!
 */
public class Game {
    /*
    Basically, this class should be a "mini-server" - a server for a single game. Each Game instance will be
    centered around 1 single game - and it's name should be the name of the player that started the game! (And number
    of free spaces? I don't know - perhaps toString Method can handle this!). From now on, a SudokuPacket that is
    requesting the server needs to either a) provide the number of spaces, b) specify which Game it wants to join, or
    c) query what Games are currently active.
    a)
        If it provides the number of spaces, then it wants to create a new game. We should create a Game with the name
        of the player as the creator, and create the new gameboard, etc.
    b)
        If it provides the Game it wishes to join, then the conversation (The Socket) should be passed to that game.
    c)
        If it queries the current Games, then it should return with a list of games and their identifiers.

     */
}
