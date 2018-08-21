package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node mParent;
    private List<Node> mChildNodes;

    // The board represents the state of the node. The positions on the node's board
    // are the positions on the parent's board plus the move made from the parent
    // to reach this node state.
    private GameBoard mBoard;

    // mPlayer represents the player making a move from this node to a child node.
    // mPlayer of the root node moves first in the game.
    // Except for the root node the mPlayer property can be set by complementing the
    // parent player for 2 player games.
    private Player mPlayer;

    // mMoveToGetHere saves teh move made from the parent to reach this node state.
    // It is used to identify the recommended move in the MCTS search when this node
    // is selected by the search algorithm.
    private Move mMoveToGetHere;

    // Number of visits to node during simulation
    private int mVisitCount;

    // UCB1 value for node
    private double mValue;

    // Terminal if the move associated with node ends the game or the node has no available moves
    // If the node is a terminal node mMove should be null
    // mPlayer would represent the next player to move if the game hadn't ended
    // If the game ended in a win, mPlayer associated with a terminal node is the loser.
    //
    // Terminal status is not known until the parent's move has been executed
    // which results in the game state transitioning to this node.
    //
    private boolean mTerminalNode;

    public String getName() {
        return mName;
    }

    private String mName;
    private List<? extends Move> mAvailableMoves;

    // Constructors

    // The root node is constructed with a board and player
    // All other nodes are child nodes and can be constructed with a private constructor
    // and using an add node method
    public Node(GameBoard gameBoard, Player firstToMove){
        mBoard =gameBoard;
        mPlayer = firstToMove;
        mParent = null;
        mTerminalNode = false;

        if (mBoard == null){
            throw new IllegalArgumentException("Game board can not be null");
        }

        mAvailableMoves =  mBoard.getAvailableMoves();
        mMoveToGetHere= null;
        mChildNodes = new ArrayList<>();
        mVisitCount = 0;
        mValue = 0d;
        mName="ROOT";
    }


    public void setPlayer(Player player){
        mPlayer = player;
    }

    public void setBoard(GameBoard board){
        mBoard = board.getCopyOfBoard();
    }

    public Move getRandomAvailableMove(){
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
        return  mMoveToGetHere;
    }

    public GameBoard getCopyOfBoard(){
        return mBoard.getCopyOfBoard();
    }

    public Player getPlayer(){
        return mPlayer;
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
        boolean hasAvailableMoves = ! mAvailableMoves.isEmpty();
        return !mTerminalNode && (hasChildren || hasAvailableMoves) ;
    }

    public boolean fullyExpanded(){
        // As child nodes are created, the corresponding moves from available moves are
        // removed from the available moves list. The node is fully expanded is
        // the available moves list is empty
        return mAvailableMoves.isEmpty();
    }



    public Node addChild(GameBoard board, boolean terminalStatus, Move moveToReachChild ){
        Node childNode = new Node(board, this.mPlayer.getOpponent());
        childNode.mParent = this;
        childNode.mTerminalNode = terminalStatus;
        childNode.mMoveToGetHere = moveToReachChild;
        childNode.mName = this.mName + " + " + moveToReachChild.getName();
        this.mChildNodes.add(childNode);
        return childNode;
    }
}
