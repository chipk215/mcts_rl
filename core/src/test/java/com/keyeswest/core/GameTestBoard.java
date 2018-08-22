package com.keyeswest.core;



import java.util.List;
import java.util.logging.Logger;

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
    public GameStatus performMove( Move move,Player player) {

        return GameStatus.IN_PROGRESS;
    }



    @Override
    public void display(Logger logger) {

    }
}
