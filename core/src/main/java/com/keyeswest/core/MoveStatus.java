package com.keyeswest.core;

public abstract class MoveStatus {

    protected Player mPlayer;
    protected boolean mValid;

    protected MoveStatus(Player player, boolean valid){
        mPlayer = player;
        mValid = valid;
    }

    public Player getPlayer(){
        return mPlayer;
    }

    public boolean isValid() {
        return mValid;
    }
}
