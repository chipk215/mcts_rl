package com.keyeswest.core;



public class Game {


    private Player mNextToMove;

    private GameBoard mGameBoard;

    private GameStatus mStatus;

    private Game(Game game){
        mNextToMove = game.mNextToMove;
        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mStatus = game.mStatus.makeCopy();
    }

    public Game(GameBoard board,Player initialPlayer ){
        mGameBoard = board;
        mNextToMove = initialPlayer;
        mStatus = new GameStatus();
    }

    public GameBoard getGameBoard() {
        return mGameBoard;

    }

    public Player togglePlayer(){
        mNextToMove = mNextToMove.getOpponent();
        return mNextToMove;
    }



    public Game makeCopy(){
        return new Game(this);
    }


}
