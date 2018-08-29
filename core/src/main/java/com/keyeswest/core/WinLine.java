package com.keyeswest.core;

import java.util.List;

public class WinLine {

    private LineType mLineType;
    private int mStartRow;
    private int mStartColumn;

    private List<Coordinate> mWinningPositions;

    public WinLine(LineType lineType, int startRow, int startColumn){
        mLineType = lineType;
        mStartRow = startRow;
        mStartColumn = startColumn;
    }

    public WinLine(LineType lineType,List<Coordinate> winPositions){
        mLineType = lineType;
        mWinningPositions = winPositions;
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

    public List<Coordinate> getWinningPositions(){
        return mWinningPositions;
    }

}
