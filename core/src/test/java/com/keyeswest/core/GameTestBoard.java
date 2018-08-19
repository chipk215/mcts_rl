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
    public MoveStatus performMove(Player player, Move move) {

        return new TestGameMoveStatus(player,true);
    }

    @Override
    public GameStatus updateGameStatus(GameStatus gameStatus, MoveStatus lastMove) {
        return null;
    }
}
