package com.keyeswest.tictactoe;

import com.keyeswest.core.*;

import java.util.ArrayList;
import java.util.List;


public class TicTacToeBoard extends GameBoard {

    private static final int ROWS = 3;
    private static final int COLUMNS = 3;


    public TicTacToeBoard(){
        super(ROWS,COLUMNS);
        for(int row = 0; row< MAX_ROWS; row++){
            for(int col = 0; col< MAX_COLS; col++){
                mBoard[row][col] =EMPTY;
            }
        }
    }

    private TicTacToeBoard(TicTacToeBoard board){
        this();
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
        GameStatus gameStatus = GameStatus.IN_PROGRESS;
        WinLine winLine;

        if (!(move instanceof TicTacToeMove)) {
            throw new IllegalStateException("Unrecognized game move.");
        }

        winLine = markPosition(player, ((TicTacToeMove)move).getRow(), ((TicTacToeMove)move).getColumn());
        if (winLine != null){
            gameStatus = GameStatus.GAME_WON;
        }else{
            // check for tie
            if (getAvailableMoves().size() == 0){
                gameStatus = GameStatus.GAME_TIED;
            }
        }
        return gameStatus;
    }


    public WinLine markPosition(Player player, int row, int column ){

        if (validateEmptyPosition(row, column)){
            mBoard[row][column] = player.value();
            mPositions.add(new CellOccupant(player,computeCellNumber(row,column)));

        }else{
            throw new IllegalArgumentException("Trying to mark occupied board position. Row= " +
                   Integer.toString(row) + " Column= " + Integer.toString(column));

        }

        return checkBoardForWin(player);
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

    WinLine checkBoardForWin(Player player){
        WinLine winningLine=null;

        // check verticals
        for (int column = 0; column < MAX_COLS; column++){
            int columnSum = 0;
            for (int row=0; row< MAX_ROWS; row++){
                columnSum+= mBoard[row][column];
            }
            if (columnSum == (3 * player.value())){
                winningLine = new WinLine(LineType.VERTICAL, 0, column);
                return winningLine;
            }
        }

        //check horizontals
        for (int row=0; row < MAX_ROWS; row++){
            int rowSum = 0;
            for (int column= 0; column< MAX_COLS; column++){
                rowSum+= mBoard[row][column];
            }
            if (rowSum == (3 * player.value())){
                winningLine = new WinLine(LineType.HORIZONTAL, row, 0);
                return winningLine;
            }
        }

        //check negative slope diagonal
        int diagonalSum = mBoard[2][0] + mBoard[1][1] + mBoard[0][2];
        if (diagonalSum == (3 * player.value())){
            winningLine = new WinLine(LineType.DIAGONAL, 2, 0);
            return winningLine;
        }

        //check positive diagonal
        diagonalSum = mBoard[0][0] + mBoard[1][1] + mBoard[2][2];
        if (diagonalSum == (3 * player.value())){
            winningLine = new WinLine(LineType.DIAGONAL, 0, 0);
            return winningLine;
        }

        return winningLine;
    }
}
