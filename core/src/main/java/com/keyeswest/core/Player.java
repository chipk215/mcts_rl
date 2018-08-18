package com.keyeswest.core;

public enum Player {
    P1(3),P2(23);

    private final int value;

    public int value(){return value;}

    Player(int value){

        this.value = value;
    }
    public Player getOpponent(){
        if (this == Player.P1){
            return Player.P2;
        }
        return Player.P1;
    }


}
