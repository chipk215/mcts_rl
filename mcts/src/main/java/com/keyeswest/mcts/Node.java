package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;
import com.keyeswest.core.GameStatus;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private Node mParent;
    private List<Node> mChildNodes;
    private GameBoard mBoard;
    private Player mPlayer;
    private int mVisitCount;
    private double mValue;
    private Move mMove;

    // Terminal if the move associated with node ends the game or the node has no available moves
    private boolean mTerminalNode;

    public String getName() {
        return mName;
    }

    private String mName;
    private List<? extends Move> mAvailableMoves;

    public Node( GameBoard board){
        this(null, board, null, null, GameStatus.IN_PROGRESS);
    }

    public Node(Node parent, GameBoard board, Player player, Move move, GameStatus gameStatus){

        mParent = parent;
        mChildNodes = new ArrayList<>();
        mBoard = board;
        mPlayer = player;
        mVisitCount = 0;
        mValue = 0d;
        mMove = move;

        if (board == null){
            throw new IllegalArgumentException("Game board can not be null");
        }
        mAvailableMoves =  mBoard.getAvailableMoves();

        // terminal node if game ended
        mTerminalNode = gameStatus != GameStatus.IN_PROGRESS;


        if (parent==null){
            mName="ROOT";
        }else{
            mName= parent.mName + " + " + mMove.getName();
        }

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

    public void setValue(double value) {
        mValue = value;
    }

    public List<Node> getChildNodes(){
        return mChildNodes;
    }

    public Move getMove(){
        return mMove;
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

        return !mTerminalNode;
    }

    public boolean fullyExpanded(){
        return mChildNodes.size() == mBoard.getAvailableMoves().size();
    }


    public void addChild(Node childNode){
        // ensure parent pointer is set
        childNode.mParent = this;
        mChildNodes.add(childNode);
    }
}
