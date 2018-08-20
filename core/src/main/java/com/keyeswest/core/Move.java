package com.keyeswest.core;

public abstract class Move {
    protected  String mName;

    public String getName(){
        return mName;
    }

    public Move(String name){
        mName = name;
    }
}
