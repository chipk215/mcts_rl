package com.keyeswest.core;

public class MoveValue {
    private Move mMove;

    private double mValue;

    public MoveValue(Move move, double value){
        mMove = move;
        mValue = value;
    }

    public Move getMove() {
        return mMove;
    }

    public double getValue() {
        return mValue;
    }

}
