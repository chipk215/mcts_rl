package com.keyeswest.mcts;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameState;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.logging.Logger;

public class MonteCarloTreeSearch {


    private static final double Cp = 1/ Math.sqrt(2);
    private static final double WIN_VALUE = 1.0d;
    private static final double LOSS_VALUE = 0.0d;
    private static final double TIE_VALUE = 0.5d;

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());


    public Move findNextMove(Game game){

        // create the search tree
        Tree tree = new Tree(game.getGameBoard());
        Node rootNode = tree.getRootNode();
        // Do we need to save the player in the node?
        // The active player is in the game state.
        rootNode.setPlayer(game.getNextPlayerToMove());


        int iterationCount = 0;
        while(iterationCount < 3000){

            Node candidateNode = treePolicy(rootNode);
            Game gameCopy = new Game(candidateNode.getCopyOfBoard(), candidateNode.getPlayer());
            GameState gameState = runSimulation(gameCopy);
            backPropagation(candidateNode, gameState);
            iterationCount++;

        }

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(rootNode,0);
        return bestChild.getMove();
    }

    public GameState runSimulation(Game game){

       // LOGGER.info("Starting game simulation");

        GameState gameStatus = game.playRandomGame();

        // alternate moves until game terminates
        return gameStatus;
    }

    private GameState rollOut(Node node){
        Game game = new Game(node.getCopyOfBoard(), node.getPlayer());
        return runSimulation(game);

    }

    private void backPropagation(Node node, GameState gameState){
        double value =  TIE_VALUE;

        if (gameState.getStatus() == GameState.Status.GAME_WON) {
            if (gameState.getWinningPlayer() == Player.P1) {
                value = WIN_VALUE;
            } else {
                value = LOSS_VALUE ;
            }
        }

        Node backPropNode = node;
        while (backPropNode != null){
            backPropNode.incrementVisit();
            backPropNode.addValue(value);
            backPropNode = backPropNode.getParent();
            value = value * -1;
        }
    }



    private Node treePolicy(Node node){
        while(node.isNonTerminal()){
            if (! node.fullyExpanded()){
                return node.expand();
            }else{
                node = UCB1.findChildNodeWithBestUCBValue(node, Cp);
            }
        }

        return node;
    }

}
