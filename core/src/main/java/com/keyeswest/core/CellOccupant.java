package com.keyeswest.core;

public class CellOccupant {

    private Player mPlayer;
    private Move mMove;

    public CellOccupant(Player player, Move move){
        mPlayer = player;
        mMove = move;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Move getMove() {
        return mMove;
    }
}
