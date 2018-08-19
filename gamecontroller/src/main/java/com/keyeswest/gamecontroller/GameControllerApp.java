package com.keyeswest.gamecontroller;

import com.keyeswest.core.Game;
import com.keyeswest.core.Player;
import com.keyeswest.fourinline.FourInLineBoard;
import com.keyeswest.mcts.MonteCarloTreeSearch;

import java.util.logging.Logger;

public class GameControllerApp {

    private static final Logger LOGGER = Logger.getLogger(GameControllerApp.class.getName());

    public static void main(String[] args){
        LOGGER.info("Start new game.");

        Game game = new Game(new FourInLineBoard(), chooseFirstMove() );

        MonteCarloTreeSearch searchAgent = new MonteCarloTreeSearch();

     //   searchAgent.runSimulation(game);


    }

    private static Player chooseFirstMove(){
        int randomSelection = (int)(Math.random() * 2);
        if ((randomSelection %2) == 0){
            return Player.P1;
        }

        return Player.P2;
    }
}
