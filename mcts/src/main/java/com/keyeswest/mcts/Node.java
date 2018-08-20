package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;
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

    private List<? extends Move> mAvailableMoves;

    public Node( GameBoard board){
        this(null, board, null, null);
    }

    private Node(Node parent, GameBoard board, Player player, Move move){

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

    }


    public void setPlayer(Player player){
        mPlayer = player;
    }

    public void setBoard(GameBoard board){
        mBoard = board.getCopyOfBoard();
    }

    public Node expand(){

        int randomSelection = (int)(Math.random() * mAvailableMoves.size());
        Move selectedMove = mAvailableMoves.remove(randomSelection);
        GameBoard board = mBoard.getCopyOfBoard();
        board.performMove(mPlayer, selectedMove );
        Node childNode = new Node(this, board, mPlayer.getOpponent(), selectedMove);
        mChildNodes.add(childNode);


        return childNode;
    }


    public boolean isLeafNode(){
        return mChildNodes.isEmpty();
    }


    public int getVisitCount() {
        return mVisitCount;
    }

    public void setVisitCount(int visitCount) {
        mVisitCount = visitCount;
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
       // return ! mChildNodes.isEmpty();
        return mBoard.getAvailableMoves().size() > 0;
    }

    public boolean fullyExpanded(){
        return mChildNodes.size() == mBoard.getAvailableMoves().size();
    }
}
