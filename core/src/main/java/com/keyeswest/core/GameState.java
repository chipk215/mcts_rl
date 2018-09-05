package com.keyeswest.core;


import java.util.List;
import java.util.logging.Logger;

/**
 * GameState represents the state of both a game in progress and the final state of a completed game.
 *
 * While in progress, GameState returns the 'IN_PROGRESS' status value.
 *
 * When complete, the progress is either 'GAME_WON' or 'GAME_TIED'. The winning player
 * is identified if the game has a winner.
 */
public final class GameState {

    private final GameBoard mGameBoard;

    //The enumerated status value for the game.
    private final GameStatus mStatus;

    private final Player mNextToMove;

    // indicates that the move that created this state blocks the opponent from winning the game
    private boolean mDefensiveState;

    private Move mMoveToGetHere;


    public GameState(GameBoard initialBoard, Player playerNextToMove,
                     GameStatus gameStatus, Move moveToGetHere){

        mGameBoard = initialBoard;

        mNextToMove = playerNextToMove;
        // Verify the next two initializations are always correct when creating a game state
        mStatus = gameStatus;
        mDefensiveState = false;
        mMoveToGetHere = moveToGetHere;


    }

    public GameState(GameState gameState){
        mGameBoard = gameState.copyBoard();
        mStatus = gameState.mStatus;
        mNextToMove = gameState.mNextToMove;
        mDefensiveState = gameState.mDefensiveState;
        mMoveToGetHere = gameState.mMoveToGetHere;
    }


    public GameStatus getStatus() {
        return mStatus;
    }


    public Player getNextToMove(){
        return mNextToMove;
    }

    public String describe(){
        StringBuilder sBuilder = new StringBuilder("Game State" + System.lineSeparator());
        sBuilder.append("Status= ").append(mStatus.toString()+ "  ");
        if (mStatus == GameStatus.GAME_TIED){
            sBuilder.append( System.lineSeparator());
        }else{
            sBuilder.append(mNextToMove.getOpponent()).append(System.lineSeparator());
        }

        return sBuilder.toString();
    }


    /**
     * Create a new game state by executing the specified move.
     * Evaluate the new game state and set state variables.
     * @param move - the move to execute from this state to the new state
     * @return
     */
    public GameState moveToNextState(Move move){

        GameBoard newBoard = mGameBoard.makeCopy();

        // execute the move on a copy of the game board associated with this state
        GameStatus gameStatus = newBoard.performMove(move,mNextToMove);

        return new GameState(newBoard,mNextToMove.getOpponent(),gameStatus, move);
    }


    public WinLine getWinLine(){
        return mGameBoard.getWinLine();
    }

    public void logBoardPositions(Logger logger){
        mGameBoard.logBoardPositions(logger);
    }

    public List<? extends Move> getAvailableMoves(){
        return mGameBoard.getAvailableMoves();
    }

    public GameBoard copyBoard(){
        return mGameBoard.makeCopy();
    }

    public void setDefensiveState(){
        mDefensiveState = true;
    }

    public boolean isDefensiveState(){
        return mDefensiveState;
    }

    public Move getStateMove(){
        return mMoveToGetHere;
    }

    public static GameState playRandomGame(GameState gameState){


        while(gameState.mStatus == GameStatus.IN_PROGRESS){
            List<? extends Move> availableMoves =  gameState.getAvailableMoves();
            int numberMovesAvailable = availableMoves.size();
            if (numberMovesAvailable ==0){
                 return new GameState(gameState.copyBoard(),gameState.getNextToMove(),
                        GameStatus.GAME_TIED,gameState.getStateMove());

            }else{
                int randomSelection = RandomIntegerGenerator.randomIntegerIndex(numberMovesAvailable);
                Move selectedMove = availableMoves.get(randomSelection);
                gameState = gameState.moveToNextState(selectedMove);
            }
        }

        return gameState;
    }

}
