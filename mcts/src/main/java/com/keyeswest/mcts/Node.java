package com.keyeswest.mcts;

import com.keyeswest.core.*;

import java.util.ArrayList;
import java.util.List;

public class Node {


    private GameState mGameState;


    private Node mParent;
    private List<Node> mChildNodes;

    // Number of visits to node during simulation
    private int mVisitCount;

    // UCB1 value for node
    private double mValue;


    public String getName() {
        return mName;
    }

    private String mName;

    // used to hold moves until node is fully expanded
    private List<? extends Move> mAvailableMoves;

    // Constructors

    public Node(GameState gameState, Node parent){
        mGameState = gameState;
        mAvailableMoves = gameState.getAvailableMoves();
        mParent = parent;
        if (parent == null){
            mName = "ROOT";
        }else{
            mName = parent.mName + " + " + gameState.getStateMove().getName();
        }

        mChildNodes = new ArrayList<>();
        mVisitCount = 0;
        mValue = 0d;

    }



    private Move getRandomAvailableChildMove(){


        if (mAvailableMoves.size() == 0){
            throw new IllegalStateException("No moves available for board, precondition requires check.");
        }
        int randomSelection = (int)(Math.random() * mAvailableMoves.size());

        return mAvailableMoves.remove(randomSelection);

    }


    public int getVisitCount() {
        return mVisitCount;
    }

    public double getValue() {
        return mValue;
    }

    public List<Node> getChildNodes(){
        return mChildNodes;
    }

    public Move getMove(){
        return  mGameState.getStateMove();
    }


    public Player getPlayer(){
        return mGameState.getNextToMove();
    }

    public void incrementVisit(){
        mVisitCount++;
    }

    public void addValue(double value){
        mValue+= value;
    }

    public Node getParent(){
        return mParent;
    }

    public boolean isNonTerminal(){
        // a node is terminal if there are no more moves available or the state of the
        // board is won or loss when the corresponding  move is executed
        boolean hasChildren = ! mChildNodes.isEmpty();
        boolean hasAvailableMoves = ! mGameState.getAvailableMoves().isEmpty();
        boolean terminalState = mGameState.getStatus() != GameStatus.IN_PROGRESS;
        return !terminalState && (hasChildren || hasAvailableMoves) ;
    }

    public boolean fullyExpanded(){
        // As child nodes are created, the corresponding moves from available moves are
        // removed from the available moves list. The node is fully expanded is
        // the available moves list is empty
        return mAvailableMoves.isEmpty();
    }



    public Node addChild(GameState newState){

        Node childNode = new Node(newState, this);
        this.mChildNodes.add(childNode);
        return childNode;
    }



    boolean getDefensiveTerminalNode(){
        return mGameState.isDefensiveState();
    }


    GameState executeRandomMove(){
        Move randomMove = getRandomAvailableChildMove();

        // before making the move for the player, make the move as the opponent to determine
        // if this is a required move to block an opponent's win
        GameBoard copyBoard = mGameState.copyBoard();

        GameState tempState = new GameState(copyBoard,mGameState.getNextToMove().getOpponent(),
                GameStatus.IN_PROGRESS, mGameState.getStateMove() );
        GameState defenseState = tempState.moveToNextState(randomMove);
        boolean defensiveMove = false;
        if (defenseState.getStatus() == GameStatus.GAME_WON){
            defensiveMove = true;
        }

        GameState newState = mGameState.moveToNextState(randomMove);
        if (((newState.getStatus() == GameStatus.IN_PROGRESS)) && defensiveMove){
            newState.setDefensiveState();
        }

        return newState;
    }

    public GameStatus getNodeStatus(){
        return mGameState.getStatus();
    }
}
