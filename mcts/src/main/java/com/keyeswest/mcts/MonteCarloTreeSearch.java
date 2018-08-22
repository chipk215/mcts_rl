package com.keyeswest.mcts;

import com.keyeswest.core.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MonteCarloTreeSearch {

    private static final Logger LOGGER = Logger.getLogger(MonteCarloTreeSearch.class.getName());

    private static final double Cp = 1/ Math.sqrt(2);
    private static final double WIN_VALUE = 1.0d;
    private static final double LOSS_VALUE = 0.0d;
    private static final double TIE_VALUE =0.5d;
    private static final int MAX_ITERATIONS = 750;


    private FileHandler fh = null;

    public MonteCarloTreeSearch(){
        setupLogging();
    }

    public void setupLogging(){
        Path currentPath = FileSystems.getDefault().getPath(".");

        SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
        String fileName = "/logs/SearchLog_" + format.format(Calendar.getInstance().getTime()) + ".log";
        Path filePath = Paths.get(currentPath.toString(), fileName);
        try{
            fh = new FileHandler(filePath.toString());
        }catch (IOException e){
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(fh);

        LOGGER.setUseParentHandlers(false);

    }

    public Move findNextMove(Game game){

        // create the search tree
        Tree tree = new Tree(game.getGameBoard(), game.getNextPlayerToMove());
        game.getGameBoard().display(LOGGER);

        int iterationCount = 0;
        while(iterationCount < MAX_ITERATIONS){
            StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
            sBuilder.append("***Iteration:"+ iterationCount + System.lineSeparator());
            GameState gameState;
            Game gameCopy = game.makeCopy();
            Node candidateNode = treePolicy(tree.getRootNode(), gameCopy);
            sBuilder.append("Candidate Selection: " + candidateNode.getName() + System.lineSeparator());

            if (candidateNode.isNonTerminal()) {
                gameState = runSimulation(gameCopy);
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
                if ((candidateNode.getParent().equals(tree.getRootNode())) &&
                        (candidateNode.getParent().getPlayer() == tree.getRootNode().getPlayer())){
                    LOGGER.log(Level.INFO,"Selected Move: " + candidateNode.getMove().getName() + System.lineSeparator() );
                    return candidateNode.getMove();
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

        Node bestChild = UCB1.findChildNodeWithBestUCBValue(tree.getRootNode(),0);
        LOGGER.log(Level.INFO,"Selected Move: " + bestChild.getMove().getName() + System.lineSeparator() );
        return bestChild.getMove();
    }

    public GameState runSimulation(Game game){

        // evaluate the game state, the expanded node may have ended the game


        GameState gameStatus = game.playRandomGame();

        // alternate moves until game terminates
        return gameStatus;
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



    private Node expand(Node node, Game game){
        GameBoard board = node.getCopyOfBoard();
        Move availableMoveFromParent = node.getRandomAvailableMove();
        game.performMove(availableMoveFromParent);
        boolean terminalStatus = game.getGameState().getStatus() != GameStatus.IN_PROGRESS;
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
    private Node treePolicy(Node node, Game game){

        // while a node has actions (steps) that can be taken
        while(node.isNonTerminal()){
            // Is each available action represented by a child node?
            if (! node.fullyExpanded()){
                // add a child node corresponding to one of the unrepresented actions
                return expand(node, game);
            }else{
                // all actions are represented so choose a child node to run
                // the simulation on based upon exploitation and exploration
                node = UCB1.findChildNodeWithBestUCBValue(node, Cp);
            }
        }

        return node;
    }

}
