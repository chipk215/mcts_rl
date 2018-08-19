package com.keyeswest.gamecontroller;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameStatus;
import com.keyeswest.core.Player;
import com.keyeswest.fourinline.FourInLineBoard;
import com.keyeswest.mcts.MonteCarloTreeSearch;

import java.util.logging.Logger;

public class GameControllerApp {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());

    public static void main(String[] args){

        for (int i=0; i<10; i++) {
            LOGGER.info("Start new game: " + Integer.toString(i+1));

            Game game = new Game(new FourInLineBoard(), chooseFirstMove());

            MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

            GameStatus gameStatus = searchAgent.runSimulation(game);

            if (gameStatus.getStatus() == GameStatus.Status.GAME_WON) {
                LOGGER.info("Game over. Winner: " + gameStatus.getWinningPlayer().toString());
            } else {
                LOGGER.info("Game over. Tie game");
            }

            LOGGER.info("Number of moves in game: " + gameStatus.getNumberOfMoves());


        }
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
}
