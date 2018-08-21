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

    //The enumerated status value for the game.
    private GameStatus mStatus;

    // Winning player if game was won, null otherwise.
    private Player mWinningPlayer;

    private Player mNextToMove;

    public void setNumberMoves(int numberMoves) {
        mNumberMoves = numberMoves;
    }

    private int mNumberMoves;

    //Copy constructor for used when making copies of games.
    private GameState(GameState status){
        mStatus = status.mStatus;
        mWinningPlayer = status.mWinningPlayer;
        mNextToMove = status.mNextToMove;
        mNumberMoves = status.mNumberMoves;
    }

    GameState(Player initialPlayer){
        mStatus = GameStatus.IN_PROGRESS;
        mWinningPlayer = null;
        mNextToMove = initialPlayer;
        mNumberMoves = 0;
    }

    public void setWinningPlayer(Player winningPlayer) {
        mWinningPlayer = winningPlayer;
    }


    public GameStatus getStatus() {
        return mStatus;
    }

    public Player getWinningPlayer() {
        return mWinningPlayer;
    }

    public void setStatus(GameStatus status) {
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

    public String describe(){
        StringBuilder sBuilder = new StringBuilder("Game State" + System.lineSeparator());
        sBuilder.append("Status= " + mStatus.toString() + System.lineSeparator());
        if (mWinningPlayer != null){
            sBuilder.append("Winner= " +  mWinningPlayer.toString()+ System.lineSeparator());
        }
        sBuilder.append("Number of Moves= " +Integer.toString(mNumberMoves) + System.lineSeparator());

        return sBuilder.toString();
    }
}
