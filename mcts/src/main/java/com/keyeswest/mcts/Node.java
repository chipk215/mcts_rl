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

    // constructors
    public Node(){
        this(null, null,null,null);
    }

    private Node(Node parent, GameBoard board, Player player, Move move){

        mParent = parent;
        mChildNodes = new ArrayList<>();
        mBoard = board;
        mPlayer = player;
        mVisitCount = 0;
        mValue = 0d;
        mMove = move;
    }


    public void setPlayer(Player player){
        mPlayer = player;
    }

    public void setBoard(GameBoard board){
        mBoard = board.getCopyOfBoard();
    }

    public int expand(){

        List<? extends Move> availableMoves = mBoard.getAvailableMoves();
        int nodesAdded = availableMoves.size();
        for (Move move : availableMoves){
            GameBoard board = mBoard.getCopyOfBoard();
            board.performMove(mPlayer, move);
            Node childNode = new Node(this, board, mPlayer.getOpponent(), move);
            mChildNodes.add(childNode);
        }

        return nodesAdded;
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
}
