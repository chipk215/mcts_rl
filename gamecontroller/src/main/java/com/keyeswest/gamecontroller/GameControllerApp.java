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

public class GameControllerApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());
    private static FileHandler fh = null;

    private static final int MAX_FOUR_IN_LINE_ITERATIONS = 3000;

    private static final int MAX_TIC_TAC_TOE_ITERATIONS = 3000;

    private Player mFirstToMove;

    private Game mGame;

    @Override
    public void start(Stage primaryStage) {
        setupLogging();
        mFirstToMove = chooseFirstMove();
        mGame= new TicTacToeGame( mFirstToMove);

        Scene scene = new Scene(mGame.getGraphicalBoardDisplay());
        primaryStage.setScene(scene);
        primaryStage.setTitle(mGame.getName());
        primaryStage.show();

        Task<Void> task = new Task<Void>(){

            @Override
            protected Void call() {

                //  Game game = new FourInLineGame(new FourInLineBoard(), P1);
                //runGame(game,2801);


                runGame(MAX_TIC_TAC_TOE_ITERATIONS);
                return null;
            }
        };

        new Thread(task).start();


    }


    private  Player chooseFirstMove(){

    //    int randomSelection = (int)(Math.random() * 2);
    //    if ((randomSelection %2) == 0){
    //        mFirstToMove= P1;
    //    }else{
    //        mFirstToMove= P2;
     //   }

        mFirstToMove = P1;
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

    private void runGame(int maxIterations){

        if (mFirstToMove == P1){
            mGame.setUserMessage("Thinking...");
            mGame.setManualPlayerTurn(false);
        }else{
            mGame.setUserMessage("Your turn to make a move.");
            mGame.setManualPlayerTurn(true);

        }

        MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch(mGame,maxIterations,LOGGER);
        Node suggestedNode = null;
        Move p2Move = null;
        boolean done = false;
        while(! done){
            if (suggestedNode == null){
                suggestedNode = searchAgent.findNextMove(null);
            }else{
                boolean foundChild = false;
                // determine if child node corresponding to opponent's move exists
                for (Node childNode : suggestedNode.getChildNodes()){
                    if (childNode.getMove().getName().equals(p2Move.getName())){
                        childNode.setParentToNull();
                        childNode.setBoard(mGame.getGameBoard());
                        suggestedNode = searchAgent.findNextMove(childNode);
                        foundChild= true;
                        break;
                    }
                }
                if (! foundChild){
                    suggestedNode = searchAgent.findNextMove(null);
                }

            }

            Move selectedMove = suggestedNode.getMove();
            LOGGER.info("Executing suggested move for P1= " + selectedMove.getName());
            // update the game model
            mGame.performMove(selectedMove);

            mGame.getGameBoard().logBoardPositions(null);

            mGame.displayMove(selectedMove, P1);

            if (mGame.getGameState().getStatus() == GameStatus.IN_PROGRESS){
                mGame.setUserMessage("Your turn to make a move.");
                // P2 moves
                p2Move = mGame.getOpponentMove();
                mGame.performMove(p2Move);
                mGame.getGameBoard().logBoardPositions(null);
                if (mGame.getGameState().getStatus() != GameStatus.IN_PROGRESS){
                    displayEndOfGameMessage(mGame, P2);
                    done = true;
                }

            }else{
                displayEndOfGameMessage(mGame, P1);
                done = true;
            }
        }
    }


    public static void main(String[] args){
        launch(args);
    }


}
