package com.keyeswest.mcts;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameState;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.List;
import java.util.logging.Logger;

public class MonteCarloTreeSearch {

    private static final long SEARCH_DURATION_MSEC = 1000;

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());


    public Move findNextMove(Game game){
        Move selectedMove = null;
        // create the search tree
        Tree tree = new Tree();
        Node rootNode = tree.getRootNode();
        rootNode.setPlayer(game.getNextPlayerToMove());
        rootNode.setBoard(game.getGameBoard());
        int nodesAdded =rootNode.expand();

        Node currentNode = rootNode;

        boolean done = nodesAdded == 0;
        int iterationCount = 0;
        while(! done){

            iterationCount++;
            if (iterationCount > 5000){
                Node selectNode = UCB1.findNodeWithBestUCBValue(rootNode);
                selectedMove = selectNode.getMove();
                break;
            }

            if (currentNode.isLeafNode()){
                if (currentNode.getVisitCount() == 0){
                    //rollOut
                    GameState gameState = rollOut(currentNode);
                    backPropagation(currentNode, gameState);
                    currentNode = rootNode;
                }
                else{
                    nodesAdded = currentNode.expand();
                    if (nodesAdded == 0){
                        // no more moves to make
                        Node selectNode = UCB1.findNodeWithBestUCBValue(rootNode);
                        selectedMove = selectNode.getMove();
                        done = true;
                    }else{
                        currentNode = currentNode.getChildNodes().get(0);

                        //rollOut
                        GameState gameState = rollOut(currentNode);
                        backPropagation(currentNode, gameState);
                        currentNode = rootNode;
                    }
                }
            }else{
                currentNode = UCB1.findNodeWithBestUCBValue(currentNode);
            }
        }

        return selectedMove;
    }

    public GameState runSimulation(Game game){

        LOGGER.info("Starting game simulation");

        GameState gameStatus = game.playRandomGame();

        // alternate moves until game terminates
        return gameStatus;
    }

    private GameState rollOut(Node node){
        Game game = new Game(node.getCopyOfBoard(), node.getPlayer());
        return runSimulation(game);

    }

    private void backPropagation(Node node, GameState gameState){
        // for now assume a win for P1 corresponds to a game value of 10
        // a loss for P1 corresponds to -10
        // a tie corresponds to 0
        double value = 0d;
        if (gameState.getStatus() == GameState.Status.GAME_WON){
            if (node.getPlayer() == Player.P1){
                if (gameState.getWinningPlayer() == Player.P1){
                    value = 10d;
                }else{
                    value = -10d;
                }
            }else{
                // P2 was playing
                if (gameState.getWinningPlayer() == Player.P2){
                    value = -10d;
                }else{
                    value = 10d;
                }
            }
        }
        Node backPropNode = node;
        while (backPropNode != null){
            backPropNode.incrementVisit();
            backPropNode.addValue(value);
            backPropNode = backPropNode.getParent();
        }
    }

}
