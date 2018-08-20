package com.keyeswest.core;


/**
 * GameState represents the state of both a game in progress and the final state of a completed game.
 *
 * While in progress, GameState returns the 'IN_PROGRESS' status value.
 *
 * When complete, the progress is either 'GAME_WON' or 'GAME_TIED'. The winning player
 * is identified if the game has a winner.
 */
public class GameState {

    public enum Status{
        IN_PROGRESS, GAME_WON, GAME_TIED
    }

    //The enumerated status value for the game.
    private Status mStatus;

    // Winning player if game was won, null otherwise.
    private Player mWinningPlayer;

    private Player mNextToMove;

    private int mNumberMoves;

    //Copy constructor for used when making copies of games.
    private GameState(GameState status){
        mStatus = status.mStatus;
        mWinningPlayer = status.mWinningPlayer;
        mNextToMove = status.mNextToMove;
        mNumberMoves = status.mNumberMoves;
    }

    GameState(Player initialPlayer){
        mStatus = Status.IN_PROGRESS;
        mWinningPlayer = null;
        mNextToMove = initialPlayer;
        mNumberMoves = 0;
    }

    public void setWinningPlayer(Player winningPlayer) {
        mWinningPlayer = winningPlayer;
    }


    public Status getStatus() {
        return mStatus;
    }

    public Player getWinningPlayer() {
        return mWinningPlayer;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public GameState makeCopy(){
        return new GameState(this);
    }

    public void nextPlayersTurn(){
        mNextToMove = mNextToMove.getOpponent();
    }

    public Player getNextToMove(){
        return mNextToMove;
    }

    public int getNumberOfMoves(){
        return mNumberMoves;
    }

    public void incrementMoveCount(){
        mNumberMoves++;
    }
}
