package com.keyeswest.core;


import javafx.scene.Parent;

import java.util.List;

public abstract class Game implements GraphicalDisplayInterface {


    protected GameBoard mGameBoard;

    protected GameState mGameState;

    protected Game(){}

    protected Game(GameBoard board,Player initialPlayer ){
        mGameBoard = board;
        mGameState = new GameState(initialPlayer);
    }

    public GameBoard getGameBoard() {
        return mGameBoard;

    }

    public abstract Parent getGraphicalBoardDisplay();

    public void setWinner(Player player){
        mGameState.setWinningPlayer(player);
    }

    public Player getWinner(){
        return mGameState.getWinningPlayer();
    }

    public void setGameStatus(GameStatus gameStatus){
        mGameState.setStatus(gameStatus);
    }

    public abstract Game makeCopy();

    public abstract String getName();

    public GameStatus getGameStatus(){
        return mGameState.getStatus();
    }

    public GameState playRandomGame(){
        while(mGameState.getStatus() == GameStatus.IN_PROGRESS){
            List<? extends Move> availableMoves =  mGameBoard.getAvailableMoves();
            int numberMovesAvailable = availableMoves.size();
            if (numberMovesAvailable ==0){
                mGameState.setStatus(GameStatus.GAME_TIED);
            }else{
                int randomSelection = (int)(Math.random() * numberMovesAvailable);
                Move selectedMove = availableMoves.get(randomSelection);
                performMove(selectedMove);
            }
        }

        return mGameState;
    }

    public Player getNextPlayerToMove(){
        return mGameState.getNextToMove();
    }

    public GameState getGameState(){
        return mGameState;
    }

    public void performMove(Move move) {

        Player player = mGameState.getNextToMove();
        GameStatus gameStatus =mGameBoard.performMove(move,player);
        Player gameWinner = null;
        if (gameStatus == GameStatus.GAME_WON){
           gameWinner =  player;
        }
        mGameState.update(gameStatus, gameWinner);


    }

    public abstract Move getOpponentMove();
}
