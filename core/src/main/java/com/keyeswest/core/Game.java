package com.keyeswest.core;


import javafx.scene.Parent;

import java.util.HashMap;

import java.util.Map;

public abstract class Game implements GraphicalDisplayInterface {

    protected static final String THINK_MESSAGE = "Thinking...";
    protected static final String YOUR_TURN_MESSAGE = "Your (O's) turn to make a move.";
    protected static final String TIED_MESSAGE = "Game over. Tied game.";
    protected static final String OPPONENT_WIN_MESSAGE = "Winner! You win, play again?";
    protected static final String COMPUTER_WIN_MESSAGE = "Computer Wins! Play again?";


    protected static final Map<UserMessages,String> USER_MESSAGES = new HashMap<UserMessages, String>(){{
        put(UserMessages.THINKING,THINK_MESSAGE);
        put(UserMessages.YOUR_TURN,YOUR_TURN_MESSAGE);
        put(UserMessages.TIED,TIED_MESSAGE);
        put(UserMessages.OPPONENT_WIN, OPPONENT_WIN_MESSAGE);
        put(UserMessages.COMPUTER_WIN,COMPUTER_WIN_MESSAGE);
    }};

    // Winning player if game was won, null otherwise.
    protected Player mWinningPlayer;

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






}
