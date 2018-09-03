package com.keyeswest.fourinline;

import com.keyeswest.core.Move;

public class FourInLineMove extends Move {

    private int mColumn;
    private Integer mRow;

    public FourInLineMove (int column){
        super(Integer.toString(column));
        mColumn = column;
        mRow = null;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setRow(int row){
        mRow = row;
    }

    public int getRow(){
        return mRow;
    }

}
