package com.keyeswest.core;

/**
 * Wrapper class representing a move or action for any game.
 * This class should be overridden by each game to include move information specific to the game.
 */
public abstract class MoveStatus {

    // Player making move.
    protected Player mPlayer;

    // Indicates whether move was valid/legal.
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
