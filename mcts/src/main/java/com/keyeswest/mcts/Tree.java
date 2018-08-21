package com.keyeswest.mcts;

import com.keyeswest.core.GameBoard;
import com.keyeswest.core.Player;

public class Tree {

    private Node mRoot;

    public Tree(GameBoard board, Player firstToMove){

        mRoot = new Node(board,firstToMove );
    }

    public Node getRootNode(){
        return mRoot;
    }
}
