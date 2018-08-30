package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;
import com.keyeswest.core.Player;

public class Tree {

    private Node mRoot;

    public Tree(GameBoard board, Player firstToMove){

        mRoot = new Node(board,firstToMove );
    }

    public Tree( Node rootNode){
        mRoot = rootNode;
    }

    public Node getRootNode(){
        return mRoot;
    }

    public void setRootNode(Node node){
        mRoot = node;
    }
}
