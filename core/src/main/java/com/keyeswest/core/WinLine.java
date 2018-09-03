package com.keyeswest.core;

import java.util.ArrayList;
import java.util.List;

public class WinLine {

    private LineType mLineType;

    private List<Coordinate> mWinningPositions;



    public WinLine(LineType lineType,List<Coordinate> winPositions){
        mLineType = lineType;
        mWinningPositions = winPositions;
    }

    public LineType getLineType() {
        return mLineType;
    }


    public List<Coordinate> getWinningPositions(){
        return mWinningPositions;
    }

    public WinLine(WinLine winLine){
        mLineType = winLine.mLineType;
        mWinningPositions = new ArrayList<>();
        for (Coordinate position : winLine.mWinningPositions){
            mWinningPositions.add(position);
        }
    }

}
