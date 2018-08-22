package com.keyeswest.tictactoe;

import com.keyeswest.core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TicTacToeBoard implements GameBoard {

    private static int EMPTY = 0;
    private static int MAX_ROWS = 3;
    private static int MAX_COLS = 3;
    private static int WIN_CONNECTION = 3;

    private List<CellOccupant> mPositions = new ArrayList<>();

    // index (0,0) corresponds to bottom left of board
    protected int[] [] mBoard = new int[MAX_ROWS][MAX_COLS];

    public TicTacToeBoard(){
        for(int row = 0; row< MAX_ROWS; row++){
            for(int col = 0; col< MAX_COLS; col++){
                mBoard[row][col] =EMPTY;
            }
        }
    }

    private TicTacToeBoard(TicTacToeBoard board){
        for(int row = 0; row< MAX_ROWS; row++){
            for(int col = 0; col< MAX_COLS; col++){
                mBoard[row][col] =board.mBoard[row][col];
            }
        }
        mPositions.addAll(board.mPositions);
    }


    @Override
    public List<TicTacToeMove> getAvailableMoves() {
        List<TicTacToeMove> moves = new ArrayList<>();
        for (int column =0; column< MAX_COLS; column++){
            for (int row=0; row< MAX_ROWS; row++){
                if (mBoard[row][column] == EMPTY){
                    moves.add(new TicTacToeMove(row,column));
                }
            }
        }
        return moves;
    }

    @Override
    public GameBoard getCopyOfBoard() {
        return new TicTacToeBoard(this);
    }

    @Override
    public GameStatus performMove(Move move, Player player) {
        //GameStatus gameStatus;

        if (!(move instanceof TicTacToeMove)) {
            throw new IllegalStateException("Unrecognized game move.");
        }

        markPosition(player, ((TicTacToeMove)move).getRow(), ((TicTacToeMove)move).getColumn());

        return null;
    }

    @Override
    public void display(Logger logger) {

    }

    public void markPosition(Player player, int row, int column ){

        if (validateEmptyPosition(row, column)){
            mBoard[row][column] = player.value();
            mPositions.add(new CellOccupant(player,computeCellNumber(row,column)));

        }else{
            throw new IllegalArgumentException("Trying to mark occupied board position. Row= " +
                   Integer.toString(row) + " Column= " + Integer.toString(column));

        }


    }


    public boolean validateEmptyPosition(int row, int column){
        if ( (column < 0) || (column >= MAX_COLS)){
            throw new IndexOutOfBoundsException("Illegal and unexpected game move executed.");
        }

        if ( (row < 0) || (column >= MAX_ROWS)){
            throw new IndexOutOfBoundsException("Illegal and unexpected game move executed.");
        }

        if (mBoard[row][column]== EMPTY){
           return true;
        }

        return false;
    }


    private int computeCellNumber(int row, int column){
        return column + MAX_COLS * row;
    }
}
