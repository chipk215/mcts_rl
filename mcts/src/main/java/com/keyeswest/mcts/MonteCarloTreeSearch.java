package com.keyeswest.mcts;

import com.keyeswest.core.*;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
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

    public SearchResult findNextMove(@NotNull GameState gameState) {

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
                GameState simState = GameState.playRandomGame(copyState);

                sBuilder.append("Simulation results: ").append(System.lineSeparator());
                sBuilder.append("   ").append(simState.describe());
                sBuilder.append(System.lineSeparator());

                // As a reminder the player who made the winning move is the opposite of the player
                // whose next in turn to move which is saved in the game state
                rewardValue = computeRewardValue(simState.getStatus(), simState.getNextToMove().getOpponent());
            } else {

                // if the search node is a winning terminal node, and an immediate child of root,
                // then return the move as the selected move.
                if ((searchNode.getParent().equals(tree.getRootNode())) &&
                        (searchNode.getParent().getPlayer() == tree.getRootNode().getPlayer())) {

                    LOGGER.log(Level.INFO, "Selected Move: " + searchNode.getMove().getName()
                            + System.lineSeparator());

                    sBuilder.append("Winning move found: " + "Selected Move: ").append(searchNode.getMove().getName());
                    LOGGER.log(Level.INFO, sBuilder.toString());
                    return prepareSearchResult(tree,searchNode.getMove() );
                } else {
                    sBuilder.append("Expanding tree with terminal node. ")
                            .append(searchNode.getMove().getName()).append(System.lineSeparator());

                    GameStatus searchStatus = searchNode.getNodeStatus();
                    rewardValue = computeRewardValue(searchStatus, searchNode.getPlayer().getOpponent());
                }
            }

            backPropagation(searchNode, rewardValue);

            LOGGER.log(Level.INFO, sBuilder.toString());
            iterationCount++;
        }

        // check for defensive nodes
        for (Node child : tree.getRootNode().getChildNodes()) {
            if (child.getDefensiveTerminalNode()) {
                LOGGER.log(Level.INFO, "Selected (defensive) Move: " +
                        child.getMove().getName() + System.lineSeparator());
                return prepareSearchResult(tree,child.getMove() );
            }
        }

        StringBuilder sb = new StringBuilder(System.lineSeparator());
        sb.append("Candidate Node Values :").append(System.lineSeparator());
        for (Node cNode : tree.getRootNode().getChildNodes()) {
            sb.append("Node: ").append(cNode.getName());
            sb.append("  Value: ").append(Double.toString(cNode.getValue() / cNode.getVisitCount()))
                    .append(System.lineSeparator());
        }
        LOGGER.log(Level.INFO, sb.toString());

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(tree.getRootNode(), 0, LOGGER);
        LOGGER.log(Level.INFO, "Selected Move: " + bestChild.getMove().getName() + System.lineSeparator());

        return prepareSearchResult(tree,bestChild.getMove() );
    }


    private SearchResult prepareSearchResult(Tree tree, Move selectedMove){
        List<MoveValue> candidates = new ArrayList<>();
        for (Node node : tree.getRootNode().getChildNodes()){

            candidates.add(new MoveValue(node.getMove(), node.getAverageValue()));
        }

        return new SearchResult(candidates, selectedMove);
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


    private static double computeRewardValue(GameStatus status, Player player) {
        if (status == GameStatus.GAME_TIED) {
            return TIE_VALUE;
        }
        if (player == Player.P1) {
            return WIN_VALUE;
        }
        return LOSS_VALUE;
    }

}
