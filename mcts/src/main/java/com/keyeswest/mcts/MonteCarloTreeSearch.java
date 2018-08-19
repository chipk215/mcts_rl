package com.keyeswest.mcts;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameStatus;
import com.keyeswest.core.Move;

import java.util.logging.Logger;

public class MonteCarloTreeSearch {

    private static final long SEARCH_DURATION_MSEC = 1000;

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());


    public Move findNextMove(Game game){
        return null;
    }

    public Game runSimulation(Game game){

        LOGGER.info("Starting game simulation");
        // make a copy of the game
        Game gameCopy = game.makeCopy();


        // alternate moves until game terminates
        return game;
    }

}
