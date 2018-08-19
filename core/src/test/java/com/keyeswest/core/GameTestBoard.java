package com.keyeswest.core;

import java.util.List;

public class GameTestBoard implements GameBoard {

    private Integer mBoard;

    public GameTestBoard(){
        mBoard = 1;
    }

    @Override
    public List<? extends Move> getAvailableMoves() {
        return null;
    }

    @Override
    public GameBoard getCopyOfBoard() {
        return null;
    }

    @Override
    public boolean performMove(Player player, Move move) {
        return false;
    }
}
