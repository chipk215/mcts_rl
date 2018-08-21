package com.keyeswest.gamecontroller;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.FourInLineBoard;
import com.keyeswest.fourinline.FourInLineMove;
import com.keyeswest.mcts.MonteCarloTreeSearch;


import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameControllerApp {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());


    public static void main(String[] args){

        Scanner in = new Scanner(System.in);
        //runSimulations();

        Game fourInLineGame = new Game(new FourInLineBoard(),Player.P1);

/*
        FourInLineMove p1Move = new FourInLineMove(3);
        FourInLineMove p2MoveInit = new FourInLineMove(0);


        // set up initial condition
        MoveStatus p1Status = fourInLineGame.getGameBoard().performMove(Player.P1,p1Move);
        updateGameState(fourInLineGame, p1Status);
        MoveStatus p2Status = fourInLineGame.getGameBoard().performMove(Player.P2,p2MoveInit);
        updateGameState(fourInLineGame, p2Status);

        p1Status = fourInLineGame.getGameBoard().performMove(Player.P1,p1Move);
        updateGameState(fourInLineGame, p1Status);
        p2Status = fourInLineGame.getGameBoard().performMove(Player.P2,p2MoveInit);
        updateGameState(fourInLineGame, p2Status);

        p1Status = fourInLineGame.getGameBoard().performMove(Player.P1,p1Move);
        updateGameState(fourInLineGame, p1Status);
        p2MoveInit = new FourInLineMove(1);
        p2Status = fourInLineGame.getGameBoard().performMove(Player.P2,p2MoveInit);
        updateGameState(fourInLineGame, p2Status);
        fourInLineGame.getGameBoard().display();

*/

        MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

        boolean done = false;
        while(! done){
            Move suggestedMove = searchAgent.findNextMove(fourInLineGame);
            if (suggestedMove instanceof FourInLineMove) {
                int column = ((FourInLineMove) suggestedMove).getColumn();
                LOGGER.info("Executing suggested move for P1= " + Integer.toString(column));
                MoveStatus moveStatus = fourInLineGame.getGameBoard().performMove(Player.P1,suggestedMove);
                done = updateGameState(fourInLineGame, moveStatus);
                fourInLineGame.getGameBoard().display();
                if (! done){
                    // P2 moves
                    System.out.println("Enter P2 move: ");
                    System.out.flush();
                    int selectedColumn = in.nextInt();
                    in.nextLine();
                    //  System.out.println("Enter P2 move: " + Integer.toString(selectedColumn));

                    FourInLineMove p2Move = new FourInLineMove(selectedColumn);
                    moveStatus = fourInLineGame.getGameBoard().performMove(Player.P2,p2Move);
                    done = updateGameState(fourInLineGame, moveStatus);
                    fourInLineGame.getGameBoard().display();

                }
            }else{
                LOGGER.log(Level.SEVERE,"Unexpected move error");
                System.exit(1);
            }

        }

    }


    private static boolean updateGameState(Game game, MoveStatus moveStatus){
        GameState gameState =  game.getGameState();
        gameState = game.getGameBoard().updateGameStatus(gameState, moveStatus);
        if (gameState.getStatus() == GameStatus.GAME_WON){
            LOGGER.info("Game over. Winner: " + gameState.getWinningPlayer().toString());
            return true;
        }
        if(gameState.getStatus() == GameStatus.GAME_TIED) {
            LOGGER.info("Game over. Winner: " + gameState.getWinningPlayer().toString());
            return true;
        }

        return false;
    }

    private static Player chooseFirstMove(){
        Player firstToMove;
        int randomSelection = (int)(Math.random() * 2);
        if ((randomSelection %2) == 0){
            firstToMove= Player.P1;
        }else{
            firstToMove = Player.P2;
        }

        LOGGER.info("First to move is: " + firstToMove.toString());
        return firstToMove;
    }

    private static void runSimulations(){
        for (int i=0; i<10; i++) {
            LOGGER.info("Start new game: " + Integer.toString(i+1));

            Game game = new Game(new FourInLineBoard(), chooseFirstMove());

            MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

            GameState gameState = searchAgent.runSimulation(game);

            if (gameState.getStatus() == GameStatus.GAME_WON) {
                LOGGER.info("Game over. Winner: " + gameState.getWinningPlayer().toString());
            } else {
                LOGGER.info("Game over. Tie game");
            }

            LOGGER.info("Number of moves in game: " + gameState.getNumberOfMoves());


        }
    }
}
