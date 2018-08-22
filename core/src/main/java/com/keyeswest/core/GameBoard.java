package com.keyeswest.core;



import java.util.List;
import java.util.logging.Logger;

public interface GameBoard {

    // Note: This method may require a player for some games.
    List<? extends Move> getAvailableMoves();

    GameBoard getCopyOfBoard();

    GameStatus performMove(Move move,Player player);

    void display(Logger logger);

}
