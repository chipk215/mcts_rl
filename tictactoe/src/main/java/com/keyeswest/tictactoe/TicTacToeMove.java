package com.keyeswest.tictactoe;

import com.keyeswest.core.Move;

public class TicTacToeMove extends Move {

    private int mRow;
    private int mColumn;

    public TicTacToeMove(int row, int column){
        super("(" + Integer.toString(row) + Integer.toString(column) + ")");
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
