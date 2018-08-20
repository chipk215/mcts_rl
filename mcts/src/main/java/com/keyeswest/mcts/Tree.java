package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;

public class Tree {

    private Node mRoot;

    public Tree(GameBoard board){
        mRoot = new Node(board);
    }

    public Node getRootNode(){
        return mRoot;
    }
}
