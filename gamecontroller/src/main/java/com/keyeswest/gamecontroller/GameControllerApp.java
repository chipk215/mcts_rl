package com.keyeswest.gamecontroller;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.FourInLineBoard;
import com.keyeswest.fourinline.FourInLineMove;
import com.keyeswest.mcts.MonteCarloTreeSearch;
import com.keyeswest.tictactoe.TicTacToeBoard;
import com.keyeswest.tictactoe.TicTacToeMove;


import java.util.Scanner;

import java.util.logging.Logger;

import static com.keyeswest.core.Player.P1;
import static com.keyeswest.core.Player.P2;

public class GameControllerApp {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());


    public static void main(String[] args){
        //runFourInLine();
        runTicTacToe();

    }


    private static Player chooseFirstMove(){
        Player firstToMove;
        int randomSelection = (int)(Math.random() * 2);
        if ((randomSelection %2) == 0){
            firstToMove= P1;
        }else{
            firstToMove = P2;
        }

        LOGGER.info("First to move is: " + firstToMove.toString());
        return firstToMove;
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

        fourInLineGame.getGameBoard().display(null);
    }


    private static void runTicTacToe(){
        Scanner in = new Scanner(System.in);

        Game ticTacGame = new Game(new TicTacToeBoard(), P1);

        MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

        boolean done = false;
        while(! done){
            Move suggestedMove = searchAgent.findNextMove(ticTacGame);
            LOGGER.info("Executing suggested move for P1= " + suggestedMove.getName());
            ticTacGame.performMove(suggestedMove);
            ticTacGame.getGameBoard().display(null);
            if (ticTacGame.getGameState().getStatus() == GameStatus.IN_PROGRESS){
                // P2 moves
                System.out.print("Enter P2 row move: ");
                int selectedRow = in.nextInt();
                in.nextLine();
                System.out.println("Enter P2 column move: ");
                System.out.flush();
                int selectedColumn = in.nextInt();
                in.nextLine();

                TicTacToeMove p2Move = new TicTacToeMove(selectedRow, selectedColumn);
                ticTacGame.performMove(p2Move);
                ticTacGame.getGameBoard().display(null);
                if (ticTacGame.getGameState().getStatus() != GameStatus.IN_PROGRESS){
                    displayEndOfGameMessage(ticTacGame, P2);
                    done = true;
                }

            }else{
                displayEndOfGameMessage(ticTacGame, P1);
                done = true;
            }
        }
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


    private static void runFourInLine(){
        Scanner in = new Scanner(System.in);
        //runSimulations();

        Game fourInLineGame = new Game(new FourInLineBoard(), P1);
        setupInitialConditions(fourInLineGame );

        MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

        boolean done = false;
        while(! done){
            Move suggestedMove = searchAgent.findNextMove(fourInLineGame);
            LOGGER.info("Executing suggested move for P1= " + suggestedMove.getName());
            fourInLineGame.performMove(suggestedMove);
            fourInLineGame.getGameBoard().display(null);
            if (fourInLineGame.getGameState().getStatus() == GameStatus.IN_PROGRESS){
                // P2 moves
                System.out.println("Enter P2 move: ");
                System.out.flush();
                int selectedColumn = in.nextInt();
                in.nextLine();

                FourInLineMove p2Move = new FourInLineMove(selectedColumn);
                fourInLineGame.performMove(p2Move);
                fourInLineGame.getGameBoard().display(null);
                if (fourInLineGame.getGameState().getStatus() != GameStatus.IN_PROGRESS){
                    displayEndOfGameMessage(fourInLineGame, P2);
                    done = true;
                }

            }else{
                displayEndOfGameMessage(fourInLineGame, P1);
                done = true;
            }
        }
    }
}
