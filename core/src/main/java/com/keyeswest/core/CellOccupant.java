package com.keyeswest.core;

public class CellOccupant {

    private Player mPlayer;
    private int mCellNumber;

    public CellOccupant(Player player, int cellNumber){
        mPlayer = player;
        mCellNumber = cellNumber;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public int getCellNumber() {
        return mCellNumber;
    }
}
