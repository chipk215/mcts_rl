package com.keyeswest.gamecontroller;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.FourInLineGame;

import com.keyeswest.mcts.MonteCarloTreeSearch;
import com.keyeswest.mcts.SearchResult;
import com.keyeswest.tictactoe.TicTacToeGame;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static com.keyeswest.core.Player.P1;
import static com.keyeswest.core.Player.P2;

public class GameControllerApp extends Application implements GameCallback {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());
    private static FileHandler fh = null;

    private static final int MAX_ITERATIONS = 3000;

    private static Stage pStage;

    private  MonteCarloTreeSearch mSearchAgent;

    private Game mGame;

    private GameState mNewState;

    public GameControllerApp(){
        super();
        setupLogging();

    }

    private GameType mGameType;


    private enum GameType {
        TicTacToe, FourInLine;

    }


    private void startNewGame(Player firstToMove){
        try {

            mSearchAgent = new MonteCarloTreeSearch(MAX_ITERATIONS, LOGGER);

            Scene scene = new Scene(mGame.getGraphicalBoardDisplay());
            pStage.setScene(scene);
            pStage.setTitle(mGame.getName());
            pStage.show();

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() {

                    if (firstToMove == P1) {
                        mGame.setUserMessage(UserMessages.THINKING);
                        mGame.setManualPlayerTurn(false);
                        executeComputerMove(mGame.getGameState());
                    } else {
                        mGame.setUserMessage(UserMessages.YOUR_TURN);
                        mGame.setManualPlayerTurn(true);

                    }
                    return null;
                }
            };

            new Thread(task).start();
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(-1);
        }

    }

    private void startNewFourInLineGame(Player firstToMove){
        mGameType = GameType.FourInLine;
        mGame = new FourInLineGame(firstToMove, this);
        startNewGame(firstToMove);

    }

    // this should execute on the UI thread
    private void startNewTicTacGame(Player firstToMove){
        mGameType = GameType.TicTacToe;
        mGame = new TicTacToeGame(firstToMove, this);
        startNewGame(firstToMove);

    }

    @Override
    public void start(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        startNewFourInLineGame(P1);
       // startNewTicTacGame(P1);
    }


    private  Player chooseFirstMove(){
        Player firstToMove;
        int randomSelection = (int)(Math.random() * 2);
        if ((randomSelection %2) == 0){
            firstToMove= P1;
        }else{
            firstToMove= P2;
        }

        LOGGER.info("First to move is: " + firstToMove.toString());
        return firstToMove;
    }


    private static void setupLogging(){
        Path currentPath = FileSystems.getDefault().getPath(".");

        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        String fileName = "/logs/SearchLog_" + format.format(Calendar.getInstance().getTime()) + ".log";
        Path filePath = Paths.get(currentPath.toString(), fileName);
        try{
            fh = new FileHandler(filePath.toString());
        }catch (IOException e){
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);

        LOGGER.setUseParentHandlers(false);

    }


    private void displayEndOfGameMessage(GameState gameState){
        if (gameState.getStatus() == GameStatus.GAME_TIED){
            LOGGER.info("Game ends in tie.");
            System.out.println("Game ends in tie.");
            mGame.setUserMessage(UserMessages.TIED);
        }else{
            Player winner = gameState.getNextToMove().getOpponent();
            LOGGER.info(winner.toString()+ " wins game.");
            System.out.println(winner.toString()+ " wins game.");

            if (winner == P1){
                mGame.setUserMessage(UserMessages.COMPUTER_WIN);
            }else{
                mGame.setUserMessage(UserMessages.OPPONENT_WIN);
            }

            // show winning line
            mGame.showWinner();
        }
    }


    private void executeComputerMove(GameState gameState){

        SearchResult searchResult = mSearchAgent.findNextMove(gameState);
        Move selectedMove = searchResult.getSelected();

        LOGGER.info("Executing suggested move for P1= " + selectedMove.getName());
        // update the game model
        mNewState = mGame.performMove(selectedMove);
        mNewState.logBoardPositions(null);

        mGame.displayMove(selectedMove, P1, searchResult.getCandidates());

    }


    private void executeManualMove(Move manualMove){

        //update the game model
        GameState gameState = mGame.performMove(manualMove);
        gameState.logBoardPositions(null);

        //update the graphical display
        mGame.displayMove(manualMove, P2, null);

        GameStatus resultStatus = gameState.getStatus();

        if (resultStatus != GameStatus.IN_PROGRESS){
            // log end log game message
            displayEndOfGameMessage(gameState);

        }else{
            // game continues, it is now the computer's move
            mGame.setUserMessage(UserMessages.THINKING);
            mGame.setManualPlayerTurn(false);
            executeComputerMove(gameState);
        }

    }


    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void opponentMove(Move move) {
        Task<Void> manualMoveTask = new Task<Void>(){

            @Override
            protected Void call() {

                executeManualMove(move);
                return null;
            }
        };

        new Thread(manualMoveTask).start();
    }

    @Override
    public void resetGame() {

        switch (mGameType){
            case TicTacToe:
                startNewTicTacGame(chooseFirstMove());
                break;
            case FourInLine:
                startNewFourInLineGame(chooseFirstMove());
                break;
        }

    }

    @Override
    public void computerMoveComplete() {
        GameStatus resultStatus = mNewState.getStatus();
        if (resultStatus == GameStatus.IN_PROGRESS){
            mGame.setUserMessage(UserMessages.YOUR_TURN);
            mGame.setManualPlayerTurn(true);

        }else{
            displayEndOfGameMessage(mNewState);

        }
    }


    private void setPrimaryStage(Stage pStage) {
        GameControllerApp.pStage = pStage;
    }
}
