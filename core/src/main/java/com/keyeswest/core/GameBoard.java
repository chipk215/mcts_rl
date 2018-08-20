package com.keyeswest.core;



import java.util.List;

public interface GameBoard {

    // Note: This method may require a player for some games.
    List<? extends Move> getAvailableMoves();

    GameBoard getCopyOfBoard();

    MoveStatus performMove(Player player, Move move);

    GameState updateGameStatus(GameState gameState, MoveStatus lastMove);

    void display();

}
