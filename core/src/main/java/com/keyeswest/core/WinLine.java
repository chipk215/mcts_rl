package com.keyeswest.core;

public class WinLine {

    private LineType mLineType;
    private int mStartRow;
    private int mStartColumn;

    public WinLine(LineType lineType, int startRow, int startColumn){
        mLineType = lineType;
        mStartRow = startRow;
        mStartColumn = startColumn;
    }

    public LineType getLineType() {
        return mLineType;
    }

    public int getStartRow() {
        return mStartRow;
    }

    public int getStartColumn() {
        return mStartColumn;
    }

}
