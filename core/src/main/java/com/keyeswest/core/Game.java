package com.keyeswest.core;


import javafx.scene.Parent;

import java.util.HashMap;

import java.util.Map;

public abstract class Game implements GraphicalDisplayInterface {



    protected GameState mGameState;

    protected  Game(GameState initialState){
        mGameState = initialState;
    }

    public abstract Parent getGraphicalBoardDisplay();

    public abstract String getName();

    public GameState performMove(Move move) {

        mGameState= mGameState.moveToNextState(move);
        return mGameState;
    }

    public GameState getGameState(){
        return mGameState;
    }




}
