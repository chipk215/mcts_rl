package com.keyeswest.mcts;

import com.keyeswest.core.*;
import com.sun.istack.internal.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MonteCarloTreeSearch {

    private static Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    private static final double Cp = 1 / Math.sqrt(2);
    private static final double WIN_VALUE = 1.0d;
    private static final double LOSS_VALUE = 0.0d;
    private static final double TIE_VALUE = 0.5d;
    private final int MAX_ITERATIONS;

    public MonteCarloTreeSearch(int iterations, Logger clientLogger) {
        if (clientLogger != null) {
            LOGGER = clientLogger;
        }

        MAX_ITERATIONS = iterations;

    }

    public Move findNextMove(@NotNull GameState gameState) {

        Tree tree = new Tree(gameState);

        gameState.logBoardPositions(LOGGER);

        int iterationCount = 0;
        while (iterationCount < MAX_ITERATIONS) {

            double rewardValue;

            StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
            sBuilder.append("***Iteration:").append(iterationCount).append(System.lineSeparator());

            Node searchNode = treePolicy(tree.getRootNode());

            sBuilder.append("Candidate Selection: ").append(searchNode.getName()).append(System.lineSeparator());


            if (searchNode.isNonTerminal()) {

                GameState copyState = new GameState(gameState);
                GameState simState = GameState.playRandomGame(copyState );

                sBuilder.append("Simulation results: " + System.lineSeparator());
                sBuilder.append("   " + simState.describe());
                sBuilder.append(System.lineSeparator());

                if (simState.getStatus() == GameStatus.GAME_TIED){
                    rewardValue = TIE_VALUE;
                }else{
                    if (simState.getNextToMove().getOpponent() == Player.P1){
                        rewardValue = WIN_VALUE;
                    }else{
                        rewardValue = LOSS_VALUE;
                    }
                }


            } else {

                // if the search node is a winning terminal node, and an immediate child of root,
                // then return the move as the selected move.
                if ((searchNode.getParent().equals(tree.getRootNode())) &&
                        (searchNode.getParent().getPlayer() == tree.getRootNode().getPlayer())) {
                    LOGGER.log(Level.INFO, "Selected Move: " + searchNode.getMove().getName() + System.lineSeparator());
                    sBuilder.append("Winning move found: " + "Selected Move: " + searchNode.getMove().getName());
                    LOGGER.log(Level.INFO, sBuilder.toString());
                    return searchNode.getMove();
                } else {
                    sBuilder.append("Expanding tree with terminal node. " +
                            searchNode.getMove().getName() + System.lineSeparator());
                    GameStatus searchStatus = searchNode.getNodeStatus();
                    if (searchStatus == GameStatus.GAME_TIED){
                        rewardValue = TIE_VALUE;
                    }else{
                        if (searchNode.getPlayer().getOpponent() == Player.P1){
                            rewardValue = WIN_VALUE;
                        }else{
                            rewardValue = LOSS_VALUE;
                        }
                    }

                }
            }

            backPropagation(searchNode, rewardValue);

            /*
            Node nodeInfo = searchNode;

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

            LOGGER.log(Level.INFO, sBuilder.toString());
            iterationCount++;
        }

        // check for defensive nodes
        for (Node child : tree.getRootNode().getChildNodes()) {
            if (child.getDefensiveTerminalNode()) {
                LOGGER.log(Level.INFO, "Selected (defensive) Move: " +
                        child.getMove().getName() + System.lineSeparator());
                return child.getMove();
            }
        }

        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Candidate Node Values :" + System.lineSeparator());
        for (Node cNode : tree.getRootNode().getChildNodes()) {
            sb.append("Node: " + cNode.getName());
            sb.append("  Value: " + Double.toString(cNode.getValue() / cNode.getVisitCount()) + System.lineSeparator());
        }
        LOGGER.log(Level.INFO, sb.toString());

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(tree.getRootNode(), 0, LOGGER);


        LOGGER.log(Level.INFO, "Selected Move: " + bestChild.getMove().getName() + System.lineSeparator());

        return bestChild.getMove();
    }

    private void backPropagation(Node node, double simReward) {


        Node backPropNode = node;
        while (backPropNode != null) {
            backPropNode.incrementVisit();
            backPropNode.addValue(simReward);
            backPropNode = backPropNode.getParent();

        }
    }


    private Node expand(Node parentNode) {
        GameState expandedState = parentNode.executeRandomMove();
        return parentNode.addChild(expandedState);
    }


    /**
     * Ensure each available action/move is chosen at least one time to run with a simulation (exploration).
     * <p>
     * After child nodes a re fully expanded, choose a candidate for simulation or further expansion via the
     * UCB1 value (exploitation).
     */
    private Node treePolicy(Node node) {

        // while a node has actions (steps) that can be taken
        while (node.isNonTerminal()) {
            // Is each available action represented by a child node?
            if (!node.fullyExpanded()) {
                // add a child node corresponding to one of the unrepresented actions
                return expand(node);
            } else {
                // all actions are represented so choose a child node to run
                // the simulation on based upon exploitation and exploration
                node = UCB1.findChildNodeWithBestUCBValue(node, Cp, null);

            }
        }
        return node;
    }

}
