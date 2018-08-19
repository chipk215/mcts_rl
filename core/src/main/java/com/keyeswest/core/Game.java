package com.keyeswest.core;


import java.util.List;

public class Game {


    private GameBoard mGameBoard;

    private GameStatus mStatus;

    private Game(Game game){

        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mStatus = game.mStatus.makeCopy();
    }

    public Game(GameBoard board,Player initialPlayer ){
        mGameBoard = board;
        mStatus = new GameStatus(initialPlayer);
    }

    public GameBoard getGameBoard() {
        return mGameBoard;

    }


    public void setWinner(Player player){
        mStatus.setWinningPlayer(player);
    }

    public Player getWinner(){
        return mStatus.getWinningPlayer();
    }

    public void setStatus(GameStatus.Status status){
        mStatus.setStatus(status);
    }

    public Game makeCopy(){
        return new Game(this);
    }

    public GameStatus.Status getStatus(){
        return mStatus.getStatus();
    }

    public GameStatus playRandomGame(){
        while(mStatus.getStatus() == GameStatus.Status.IN_PROGRESS){
            List<? extends Move> availableMoves =  mGameBoard.getAvailableMoves();
            int numberMovesAvailable = availableMoves.size();
            if (numberMovesAvailable ==0){
                mStatus.setStatus(GameStatus.Status.GAME_TIED);
            }else{
                int randomSelection = (int)(Math.random() * numberMovesAvailable);
                Move selectedMove = availableMoves.get(randomSelection);

                MoveStatus moveStatus = mGameBoard.performMove(mStatus.getNextToMove(), selectedMove);
                if (! moveStatus.mValid){
                    throw new IllegalStateException("Invalid game move");
                }
                mStatus.incrementMoveCount();
                mStatus = mGameBoard.updateGameStatus(mStatus, moveStatus);


            }
        }

        return mStatus;
    }
}
