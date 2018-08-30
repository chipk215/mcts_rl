package com.keyeswest.gamecontroller;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.FourInLineMove;
import com.keyeswest.mcts.MonteCarloTreeSearch;
import com.keyeswest.mcts.Node;
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

public class GameControllerApp extends Application implements ManualPlayerCallback  {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());
    private static FileHandler fh = null;

    private static final int MAX_FOUR_IN_LINE_ITERATIONS = 3000;
    private static final int MAX_TIC_TAC_TOE_ITERATIONS = 3000;


    private Player mFirstToMove;

    private  MonteCarloTreeSearch mSearchAgent;

    private Game mGame;


    private Node mSuggestedNode;

    public GameControllerApp(){
        super();
        setupLogging();

    }


    // this should execute on the UI thread
    private void startNewGame(Stage primaryStage){

        try {
            mFirstToMove = chooseFirstMove();
            mGame = new TicTacToeGame(mFirstToMove, this);
            //mSearchAgent = new MonteCarloTreeSearch(mGame, MAX_TIC_TAC_TOE_ITERATIONS, LOGGER);
            mSearchAgent = null;
            mSuggestedNode = null;

            Scene scene = new Scene(mGame.getGraphicalBoardDisplay());
            primaryStage.setScene(scene);
            primaryStage.setTitle(mGame.getName());
            primaryStage.show();

            Task<Void> task = new Task<Void>() {

                @Override
                protected Void call() {

                    //  Game game = new FourInLineGame(new FourInLineBoard(), P1);
                    //runGame(game,2801);
                    if (mFirstToMove == P1) {
                        mGame.setUserMessage(UserMessages.THINKING);
                        mGame.setManualPlayerTurn(false);
                        executeComputerMove(null);
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

    @Override
    public void start(Stage primaryStage) {
        startNewGame(primaryStage);
    }


    private  Player chooseFirstMove(){

        int randomSelection = (int)(Math.random() * 2);
        if ((randomSelection %2) == 0){
            mFirstToMove= P1;
        }else{
            mFirstToMove= P2;
        }

        mFirstToMove = P1.getOpponent();
        //mFirstToMove = P1;
        LOGGER.info("First to move is: " + mFirstToMove.toString());
        return mFirstToMove;
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


    private static void setupInitialConditions(Game fourInLineGame){
        FourInLineMove p1Move = new FourInLineMove(1);
        FourInLineMove p2MoveInit = new FourInLineMove(6);


        // set up initial condition
        fourInLineGame.performMove(p1Move);
        fourInLineGame.performMove(p2MoveInit);

        p1Move = new FourInLineMove(3);
        fourInLineGame.performMove(p1Move);
        fourInLineGame.performMove(p2MoveInit);

        fourInLineGame.performMove(p1Move);
        fourInLineGame.performMove(p2MoveInit);

        fourInLineGame.getGameBoard().logBoardPositions(null);
    }


    private static void displayEndOfGameMessage(Game game, Player player){
        if (game.getGameState().getStatus() == GameStatus.GAME_TIED){
            LOGGER.info("Game ends in tie.");
            System.out.println("Game ends in tie.");
        }else{
            LOGGER.info(player.toString()+ " wins game.");
            System.out.println(player.toString()+ " wins game.");
        }
    }

    private void executeComputerMove(Move opponentMove){

        //TODO All this startup logic needs to be moved into MCTS

        if (mSearchAgent == null){
            // this is the computer's first move

            // has the opponent moved?
            if (opponentMove != null){
                // yes, the opponent moved first so create a root node representing the user's move
                Node opNode = new Node(mGame.getGameBoard(), P1,  opponentMove );
                mSearchAgent = new MonteCarloTreeSearch(mGame, MAX_TIC_TAC_TOE_ITERATIONS, LOGGER, opNode);
                mSuggestedNode = mSearchAgent.findNextMove(null);
            }else{
                // computer moved first
                mSearchAgent = new MonteCarloTreeSearch(mGame, MAX_TIC_TAC_TOE_ITERATIONS, LOGGER);
                mSuggestedNode = mSearchAgent.findNextMove(null);
            }
        }else{
            // continuation of the game after the computer has moved at least once

            // if the opponent has just moved look in the search tree for the opponent move to
            // use the previously generated simulation history to help determine the next move
            boolean foundChild = false;
            // determine if child node corresponding to opponent's move exists
            for (Node childNode : mSuggestedNode.getChildNodes()) {
                if (childNode.getMove().getName().equals(opponentMove.getName())) {
                    childNode.setParentToNull();
                    childNode.setBoard(mGame.getGameBoard());
                    mSuggestedNode = mSearchAgent.findNextMove(childNode);
                    foundChild = true;
                    break;
                }
            }
            if (!foundChild) {
                // if the opponent move is not present then create a new root node representing the user move
                Node opNode = new Node(mGame.getGameBoard(), P2,  opponentMove );
                mSearchAgent = new MonteCarloTreeSearch(mGame, MAX_TIC_TAC_TOE_ITERATIONS, LOGGER, opNode);
                mSuggestedNode = mSearchAgent.findNextMove(null);
            }


        }


        Move selectedMove = mSuggestedNode.getMove();
        LOGGER.info("Executing suggested move for P1= " + selectedMove.getName());
        // update the game model
        mGame.performMove(selectedMove);

        mGame.getGameBoard().logBoardPositions(null);

        mGame.displayMove(selectedMove, P1);

        GameStatus resultStatus = mGame.getGameState().getStatus();
        if (resultStatus == GameStatus.IN_PROGRESS){
            mGame.setUserMessage(UserMessages.YOUR_TURN);
            mGame.setManualPlayerTurn(true);

        }else{
            displayEndOfGameMessage(mGame, P1);
            if (resultStatus == GameStatus.GAME_WON){
                // handle game won scenario
                displayWinnerInformation();
            }else{
                mGame.setUserMessage(UserMessages.TIED);
            }

        }

    }


    private void displayWinnerInformation(){
        Player winner = mGame.getGameState().getWinningPlayer();
        if (winner == P1){
            mGame.setUserMessage(UserMessages.COMPUTER_WIN);
        }else{
            mGame.setUserMessage(UserMessages.OPPONENT_WIN);
        }

        // show winning line
        mGame.showWinner();

    }


    private void executeManualMove(Move manualMove){

        //update the game model
        mGame.performMove(manualMove);
        mGame.getGameBoard().logBoardPositions(null);

        //update the graphical display
        mGame.displayMove(manualMove, P2);

        GameStatus resultStatus = mGame.getGameState().getStatus();

        if (resultStatus != GameStatus.IN_PROGRESS){
            // log end log game message
            displayEndOfGameMessage(mGame, P2);
            if (resultStatus == GameStatus.GAME_WON){
                // handle game won scenario
                displayWinnerInformation();
            }else{
                mGame.setUserMessage(UserMessages.TIED);
            }

        }else{
            // game continues, it is now the computer's move
            mGame.setUserMessage(UserMessages.THINKING);
            mGame.setManualPlayerTurn(false);
            executeComputerMove(manualMove);
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

        new Thread( manualMoveTask).start();
    }
}
