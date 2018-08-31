package com.keyeswest.mcts;

import com.keyeswest.core.GameState;


class Tree {

    private Node mRoot;


    Tree(GameState gameState){
       mRoot = new Node(gameState, null);
    }



    Node getRootNode(){
        return mRoot;
    }

    void setRootNode(Node node){
        mRoot = node;
    }
}
