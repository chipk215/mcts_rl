package com.keyeswest.fourinline;

import com.keyeswest.core.Move;

public class FourInLineMove extends Move {

    private int mColumn;

    public FourInLineMove (int column){
        super();
        mColumn = column;
    }

    public int getColumn() {
        return mColumn;
    }

}
