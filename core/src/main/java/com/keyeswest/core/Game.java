package com.keyeswest.core;


import java.util.List;

public class Game {


    private GameBoard mGameBoard;

    private GameState mGameState;

    private Game(Game game){

        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mGameState = game.mGameState.makeCopy();
    }

    public Game(GameBoard board,Player initialPlayer ){
        mGameBoard = board;
        mGameState = new GameState(initialPlayer);
    }

    public GameBoard getGameBoard() {
        return mGameBoard;

    }


    public void setWinner(Player player){
        mGameState.setWinningPlayer(player);
    }

    public Player getWinner(){
        return mGameState.getWinningPlayer();
    }

    public void setGameStatus(GameStatus gameStatus){
        mGameState.setStatus(gameStatus);
    }

    public Game makeCopy(){
        return new Game(this);
    }

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

                MoveStatus moveStatus = mGameBoard.performMove(mGameState.getNextToMove(), selectedMove);
                if (! moveStatus.mValid){
                    throw new IllegalStateException("Invalid game move");
                }
                mGameState.incrementMoveCount();
                mGameState = mGameBoard.updateGameStatus(mGameState, moveStatus);


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
}
