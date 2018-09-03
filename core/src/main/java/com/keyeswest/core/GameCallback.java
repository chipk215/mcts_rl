package com.keyeswest.core;

public interface GameCallback {

    void opponentMove(Move move);
    void resetGame();
    void computerMoveComplete();
}
