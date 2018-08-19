package com.keyeswest.fourinline;

import com.keyeswest.core.MoveStatus;
import com.keyeswest.core.Player;

public class FourInLineMoveStatus extends MoveStatus {

    private int mRow;
    private int mColumn;


    public FourInLineMoveStatus(Player player, int row, int column, boolean valid){
        super(player, valid);
        mRow = row;
        mColumn = column;
    }

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }


}
