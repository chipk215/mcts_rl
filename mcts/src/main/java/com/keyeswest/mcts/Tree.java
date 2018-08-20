package com.keyeswest.mcts;

public class Tree {

    private Node mRoot;

    public Tree(){
        mRoot = new Node();
    }

    public Node getRootNode(){
        return mRoot;
    }
}
