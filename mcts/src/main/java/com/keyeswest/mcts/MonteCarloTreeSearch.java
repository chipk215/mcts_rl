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

    public MonteCarloTreeSearch(Game game, int iterations, Logger clientLogger, Node rootNode){
        if (clientLogger != null){
            LOGGER = clientLogger;
        }

        MAX_ITERATIONS = iterations;
        mGame = game;
        mTree = new Tree(rootNode);
    }

    public void updateGame(Game game){
        mGame = game;
        mTree = new Tree(game.getGameBoard(), game.getNextPlayerToMove());
    }
    public Node findNextMove(Node node){

        if (node != null) {
            mTree.setRootNode(node);
        }
        // check for defensive nodes
        for (Node child : mTree.getRootNode().getChildNodes()) {
            GameBoard defenseBoard = node.getCopyOfBoard();

            Player opponent = mTree.getRootNode().getPlayer().getOpponent();
            Move opMove = child.getMove();
            GameStatus defenseStatus = defenseBoard.performMove(opMove, opponent);

            if (defenseStatus == GameStatus.GAME_WON) {
                child.setDefensiveTerminalNode();
                // all we need is one defensive move, if there are more than 1 we are going to lose
                break;
            } else {
                child.clearDefensiveTerminalNode();
            }
        }

        mGame.getGameBoard().logBoardPositions(LOGGER);

        int iterationCount = 0;
        while(iterationCount < MAX_ITERATIONS){
            StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
            sBuilder.append("***Iteration:"+ iterationCount + System.lineSeparator());


            Node searchNode = treePolicy(mTree.getRootNode());
            sBuilder.append("Candidate Selection: " + searchNode.getName() + System.lineSeparator());
            GameState gameState;
            if (searchNode.isNonTerminal()) {
                 Game simGame = mGame.makeCopy();
                 simGame.clearGameState();
                 simGame.executeHistory(searchNode.getBoard(),searchNode.getBoardHistory());

                 gameState = simGame.playRandomGame();

                 sBuilder.append("Simulation results: " + System.lineSeparator());
                 sBuilder.append("   " + gameState.describe());
                 sBuilder.append(System.lineSeparator());
            }else {
                Game  gameCopy =  mGame.makeCopy();
                gameCopy.getGameState().update(GameStatus.GAME_WON,searchNode.getPlayer().getOpponent());
                gameState = gameCopy.getGameState();
                gameState.setStatus(GameStatus.GAME_WON);

                // if the search node is a winning terminal node, and an immediate child of root,
                // then return the move as the selected move.
                if ((searchNode.getParent().equals(mTree.getRootNode())) &&
                        (searchNode.getParent().getPlayer() == mTree.getRootNode().getPlayer())){
                    LOGGER.log(Level.INFO,"Selected Move: " + searchNode.getMove().getName() + System.lineSeparator() );
                    //return candidateNode.getMove();
                    sBuilder.append("Winning move found: " + "Selected Move: " + searchNode.getMove().getName());
                    LOGGER.log(Level.INFO,sBuilder.toString());
                    return searchNode;
                } else{
                    sBuilder.append("Expanding tree. "  + searchNode.getMove().getName() + System.lineSeparator());

                }

            }
            backPropagation(searchNode, gameState);

            Node nodeInfo = searchNode;

 /*
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
 */

            LOGGER.log(Level.INFO,sBuilder.toString());
            iterationCount++;
        }

        // check for defensive nodes
        for (Node child : mTree.getRootNode().getChildNodes()){
            if (child.getDefensiveTerminalNode()){
                LOGGER.log(Level.INFO,"Selected (defensive) Move: " +
                        child.getMove().getName() + System.lineSeparator() );
                return child;
            }
        }

        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Candidate Node Values :" + System.lineSeparator() );
        for (Node cNode : mTree.getRootNode().getChildNodes() ) {
            sb.append("Node: " + cNode.getName());
            sb.append("  Value: " + Double.toString(cNode.getValue()/cNode.getVisitCount()) + System.lineSeparator())  ;
        }
        LOGGER.log(Level.INFO,sb.toString());

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(mTree.getRootNode(),0,LOGGER);

        // log candidate node values



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


    private Node expand(Node parentNode){
        // A copy of the parent node's board is used to add the move associated with the
        // the new child

        Move availableMoveFromParent = parentNode.getRandomAvailableChildMove();
        boolean defenseTerminalState = false;
        // check for defensive terminal node if parentNode is root
        if (parentNode.getParent() == null){
            GameBoard defenseBoard = parentNode.getCopyOfBoard();
            GameStatus defenseStatus = defenseBoard.performMove(availableMoveFromParent,
                    parentNode.getPlayer().getOpponent() );
            if (defenseStatus == GameStatus.GAME_WON){
                defenseTerminalState = true;
            }
        }

        GameBoard childBoard = parentNode.getCopyOfBoard();
        GameStatus moveStatus = childBoard.performMove(availableMoveFromParent,parentNode.getPlayer());
        boolean terminalStatus = moveStatus != GameStatus.IN_PROGRESS;
        Node childNode = parentNode.addChild(childBoard,terminalStatus,availableMoveFromParent);
        if (defenseTerminalState){
            childNode.setDefensiveTerminalNode();
        }else{
            childNode.clearDefensiveTerminalNode();
        }
        return childNode;
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
