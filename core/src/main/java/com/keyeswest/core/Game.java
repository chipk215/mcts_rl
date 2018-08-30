package com.keyeswest.core;


import javafx.scene.Parent;

import java.util.HashMap;
import java.util.List;
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

    public void clearGameState(){
        mGameBoard = null;
        mGameState = null;
    }

    public void executeHistory(GameBoard board, List<CellOccupant> history){
        mGameBoard = board;
        Player initialPlayer = history.get(0).getPlayer();
        mGameState = new GameState(initialPlayer);
        for (CellOccupant occupant : history){
           performMove(occupant.getMove());
        }
    }

    public abstract Move getOpponentMove();
}
