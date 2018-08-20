package com.keyeswest.fourinline;

import com.keyeswest.core.GameStatus;
import com.keyeswest.core.MoveStatus;
import com.keyeswest.core.Player;

public class FourInLineMoveStatus extends MoveStatus {

    private int mRow;
    private int mColumn;


    public FourInLineMoveStatus(Player player, int row, int column, boolean valid, GameStatus gameStatus){
        super(player, valid);
        mRow = row;
        mColumn = column;
        mGameStatus = gameStatus;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }


}
