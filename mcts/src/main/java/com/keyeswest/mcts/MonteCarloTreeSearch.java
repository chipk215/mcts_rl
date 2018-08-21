package com.keyeswest.mcts;

import com.keyeswest.core.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MonteCarloTreeSearch {

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    private static final double Cp = 1/ Math.sqrt(2);
    private static final double WIN_VALUE = 1.0d;
    private static final double LOSS_VALUE = 0.0d;
    private static final double TIE_VALUE =0.5d;
    private static final int MAX_ITERATIONS = 40;

    public Move findNextMove(Game game){

        // create the search tree
        Tree tree = new Tree(game.getGameBoard(), game.getNextPlayerToMove());

        int iterationCount = 0;
        while(iterationCount < MAX_ITERATIONS){
            GameState gameState;
            Node candidateNode = treePolicy(tree.getRootNode());
            Game gameCopy = new Game(candidateNode.getCopyOfBoard(), candidateNode.getPlayer());
            if (candidateNode.isNonTerminal()) {
                gameState = runSimulation(gameCopy);
            }else{
                gameState = gameCopy.getGameState();
                gameState.setStatus(GameStatus.GAME_WON);
                gameState.setWinningPlayer(candidateNode.getPlayer().getOpponent());
                gameState.setNumberMoves(candidateNode.getGameMoves());
            }
            backPropagation(candidateNode, gameState);
            iterationCount++;

            StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
            sBuilder.append("***Iteration:"+ iterationCount + System.lineSeparator());
            Node nodeInfo = candidateNode;
            int level=0;
            while(nodeInfo != null){
                sBuilder.append("level= "+ level + System.lineSeparator());
                sBuilder.append("Name= ").append(nodeInfo.getName() + System.lineSeparator());
                sBuilder.append("Value= " + nodeInfo.getValue() + System.lineSeparator());
                sBuilder.append("Visits= ").append(nodeInfo.getVisitCount()).append(System.lineSeparator());
                sBuilder.append("---"+System.lineSeparator());
                nodeInfo = nodeInfo.getParent();
                level++;

            }

            LOGGER.log(Level.INFO,sBuilder.toString());
        }

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(tree.getRootNode(),0);
        return bestChild.getMove();
    }

    public GameState runSimulation(Game game){

        // evaluate the game state, the expanded node may have ended the game


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

        if (gameState.getStatus() == GameStatus.GAME_WON) {
            if (gameState.getWinningPlayer() == Player.P1) {
                value = WIN_VALUE;
            } else{
                value = LOSS_VALUE;
            }
        }



        Node backPropNode = node;
        while (backPropNode != null){
            backPropNode.incrementVisit();
            backPropNode.addValue(value);
            backPropNode = backPropNode.getParent();
          /*
            if (value==WIN_VALUE){
                value = LOSS_VALUE;
            }else{
                value = WIN_VALUE;
            }
            */
        }
    }



    private Node expand(Node node){
        GameBoard board = node.getCopyOfBoard();
        Move availableMoveFromParent = node.getRandomAvailableMove();
        MoveStatus moveStatus= board.performMove(node.getPlayer(),availableMoveFromParent);
        boolean terminalStatus = moveStatus.getGameStatus() != GameStatus.IN_PROGRESS;
        return node.addChild(board,terminalStatus,availableMoveFromParent);
    }


    /**
     * Starting at the provided node (generally the root node), expand the node if the
     * node can be expanded and return the expanded node for simulation.
     *
     * If the node is fully expanded choose a child node for consideration and locate either
     * a terminal node or a non-fully expanded node in the child branch to simulate.
     *
     */
    private Node treePolicy(Node node){

        // while a node has actions (steps) that can be taken
        while(node.isNonTerminal()){
            // Is each available action represented by a child node?
            if (! node.fullyExpanded()){
                // add a child node corresponding to one of the unrepresented actions
                return expand(node);
            }else{
                // all actions are represented so choose a child node to run
                // the simulation on based upon exploitation and exploration
                node = UCB1.findChildNodeWithBestUCBValue(node, Cp);
            }
        }

        return node;
    }

}
