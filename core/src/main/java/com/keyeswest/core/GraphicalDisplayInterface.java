package com.keyeswest.core;

public interface GraphicalDisplayInterface {
    void setUserMessage(String string);
    void setManualPlayerTurn(boolean manualPlayerTurn);
    void displayMove(Move move, Player player);
}
