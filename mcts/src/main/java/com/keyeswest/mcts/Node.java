package com.keyeswest.mcts;

import com.keyeswest.core.CellOccupant;
import com.keyeswest.core.GameBoard;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.ArrayList;
import java.util.List;

public class Node {

    public void setParentToNull(){
        mParent = null;
    }
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

    // mMoveToGetHere saves the move made from the parent to reach this node state.
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


    // Indicates that if the opponent's next move is into this position the opponent will win the game.
    private boolean mDefensiveTerminalNode;

    public String getName() {
        return mName;
    }

    private String mName;

    // used to hold moves until node is fully expanded
    private List<? extends Move> mAvailableChildMoves;

    // Constructors

    // The root node is constructed with a board and player
    // All other nodes are child nodes and can be constructed with a private constructor
    // and using an add node method
    public Node(GameBoard gameBoard, Player firstToMove){
        mBoard = gameBoard;
        mPlayer = firstToMove;
        mParent = null;
        mTerminalNode = false;
        mDefensiveTerminalNode = false;

        if (mBoard == null){
            throw new IllegalArgumentException("Game board can not be null");
        }

        mAvailableChildMoves =  mBoard.getAvailableMoves();
        mMoveToGetHere= null;
        mChildNodes = new ArrayList<>();
        mVisitCount = 0;
        mValue = 0d;
        mName="ROOT";
    }

    public Node(GameBoard gameBoard, Player firstToMove, Move firstMove){
        this(gameBoard, firstToMove);
        mMoveToGetHere = firstMove;
        mName = mName + " + " + firstMove.getName();
    }


    public void setBoard(GameBoard board){
        mBoard = board.getCopyOfBoard();
    }

    public Move getRandomAvailableChildMove(){


        if (mAvailableChildMoves.size() == 0){
            throw new IllegalStateException("No moves available for board, precondition requires check.");
        }
        int randomSelection = (int)(Math.random() * mAvailableChildMoves.size());

        Move theMove =  mAvailableChildMoves.remove(randomSelection);

        return theMove;
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
        boolean hasAvailableMoves = ! mBoard.getAvailableMoves().isEmpty();
        return !mTerminalNode && (hasChildren || hasAvailableMoves) ;
    }

    public boolean fullyExpanded(){
        // As child nodes are created, the corresponding moves from available moves are
        // removed from the available moves list. The node is fully expanded is
        // the available moves list is empty
        return mAvailableChildMoves.isEmpty();
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

    public int getGameMoves(){
        int count = 0;
        Node node = this.mParent;
        while(node != null){
            node = node.mParent;
            count++;
        }

        return count;
    }

    public void setDefensiveTerminalNode(){
        mDefensiveTerminalNode = true;
    }

    public void clearDefensiveTerminalNode(){
        mDefensiveTerminalNode = false;
    }

    public boolean getDefensiveTerminalNode(){
        return mDefensiveTerminalNode;
    }

    public  List<CellOccupant> getBoardHistory(){
        return mBoard.getBoardPositions();
    }

    public GameBoard getBoard(){
        return mBoard;
    }
}
