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
    public MoveStatus performMove(Player player, Move move) {

        return new TestGameMoveStatus(player,true);
    }

    @Override
    public GameState updateGameStatus(GameState gameState, MoveStatus lastMove) {
        return null;
    }

    @Override
    public void display(Logger logger) {

    }
}
