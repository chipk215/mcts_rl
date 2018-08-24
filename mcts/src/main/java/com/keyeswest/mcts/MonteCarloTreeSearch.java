package com.keyeswest.mcts;

import com.keyeswest.core.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MonteCarloTreeSearch {

    private static Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    private static final double Cp = 1/ Math.sqrt(2);
    private static final double WIN_VALUE = 1.0d;
    private static final double LOSS_VALUE = 0.0d;
    private static final double TIE_VALUE =0.5d;
    private final int MAX_ITERATIONS;

    private Tree mTree;
    private Game mGame;

    public MonteCarloTreeSearch(Game game, int iterations, Logger clientLogger){
        if (clientLogger != null){
            LOGGER = clientLogger;
        }

        MAX_ITERATIONS = iterations;
        mGame = game;
        mTree = new Tree(game.getGameBoard(), game.getNextPlayerToMove());
    }

    public void updateGame(Game game){
        mGame = game;
        mTree = new Tree(game.getGameBoard(), game.getNextPlayerToMove());
    }
    public Node findNextMove(Node node){

        if (node != null){
            mTree.setRootNode(node);
        }
        mGame.getGameBoard().display(LOGGER);

        int iterationCount = 0;
        while(iterationCount < MAX_ITERATIONS){
            StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
            sBuilder.append("***Iteration:"+ iterationCount + System.lineSeparator());
            GameState gameState;

            Node candidateNode = treePolicy(mTree.getRootNode());
            sBuilder.append("Candidate Selection: " + candidateNode.getName() + System.lineSeparator());
            Game gameCopy = mGame.makeCopy();
            if (candidateNode.isNonTerminal()) {
                gameState = gameCopy.playRandomGame();
                sBuilder.append("Simulation results: " + System.lineSeparator());
                sBuilder.append("   " + gameState.describe());
                sBuilder.append(System.lineSeparator());
            }else {
                gameCopy.getGameState().update(GameStatus.GAME_WON,candidateNode.getPlayer().getOpponent());
                gameState = gameCopy.getGameState();
                gameState.setStatus(GameStatus.GAME_WON);

                // if the terminal node represents a winning move for root node player
                // then return the move as the selected move.
                //TODO - require the terminal node to be child of rode in order to terminate search
                if ((candidateNode.getParent().equals(mTree.getRootNode())) &&
                        (candidateNode.getParent().getPlayer() == mTree.getRootNode().getPlayer())){
                    LOGGER.log(Level.INFO,"Selected Move: " + candidateNode.getMove().getName() + System.lineSeparator() );
                    //return candidateNode.getMove();
                    return candidateNode;
                }

            }
            backPropagation(candidateNode, gameState);

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
            iterationCount++;
        }

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(mTree.getRootNode(),0,LOGGER);
        LOGGER.log(Level.INFO,"Selected Move: " + bestChild.getMove().getName() + System.lineSeparator() );
        //return bestChild.getMove();
        return bestChild;
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

        }
    }


    private Node expand(Node node){
        // A copy of the parent node's board is used to add the move associated with the
        // the new child
        GameBoard board = node.getCopyOfBoard();
        Move availableMoveFromParent = node.getRandomAvailableMove();
        GameStatus moveStatus = board.performMove(availableMoveFromParent,node.getPlayer());
        boolean terminalStatus = moveStatus != GameStatus.IN_PROGRESS;

        return node.addChild(board,terminalStatus,availableMoveFromParent);
    }


    /**
     * Ensure each available action/move is chosen at least one time to run with a simulation (exploration).
     *
     * After child nodes a re fully expanded, choose a candidate for simulation or further expansion via the
     * UCB1 value (exploitation).
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
                node = UCB1.findChildNodeWithBestUCBValue(node, Cp, null);
            }
        }

        return node;
    }

}
