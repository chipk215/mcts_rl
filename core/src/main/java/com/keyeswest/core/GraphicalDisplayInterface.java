package com.keyeswest.core;

import java.util.List;

public interface GraphicalDisplayInterface {
    void setUserMessage(UserMessages message);
    void setManualPlayerTurn(boolean manualPlayerTurn);
    void displayMove(Move move, Player player);
    void showWinner();
}
