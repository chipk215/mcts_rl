package com.keyeswest.core;

import java.util.List;

public interface GameBoard {

    List<? extends Move> getAvailableMoves();

    GameBoard getCopyOfBoard();

    boolean performMove(Player player, Move move);

}
