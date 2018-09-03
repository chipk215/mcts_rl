package com.keyeswest.core;


import javafx.scene.Parent;


public abstract class Game implements GraphicalDisplayInterface {

    protected static final String THINK_MESSAGE = "Thinking...";
    protected static final String TIED_MESSAGE = "Game over. Tied game.";
    protected static final String OPPONENT_WIN_MESSAGE = "Winner! You win, play again?";
    protected static final String COMPUTER_WIN_MESSAGE = "Computer Wins! Play again?";

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
