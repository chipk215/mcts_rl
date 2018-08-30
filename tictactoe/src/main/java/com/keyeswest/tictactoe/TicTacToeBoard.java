package com.keyeswest.tictactoe;

import com.keyeswest.core.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Iteration Analysis
 *
 * There are possible 9 possible first moves requiring 9 iterations to fully expand level level 1.
 *
 * There are 9*8 possible 2nd move states corresponding to 9 + 9*8 = 81 iteration.
 *
 * Third level = 9*8*7 = 504
 *
 *
 */


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

        mWinLine = board.mWinLine;
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

        try {

            markPosition(player,move);
        }catch(Exception ex){
            System.exit(-1);
        }
        if ( mWinLine != null){
            gameStatus = GameStatus.GAME_WON;
        }else{
            // check for tie
            if (getAvailableMoves().size() == 0){
                gameStatus = GameStatus.GAME_TIED;
            }
        }
        return gameStatus;
    }


    private void markPosition(Player player, Move move ){

        if (!(move instanceof TicTacToeMove)) {
            throw new IllegalStateException("Unrecognized game move.");
        }
        int row = ((TicTacToeMove) move).getRow();
        int column = ((TicTacToeMove) move).getColumn();
        if (validateEmptyPosition(row, column)){
            mBoard[row][column] = player.value();
            mPositions.add(new CellOccupant(player,move));

        }else{
            throw new IllegalArgumentException("Trying to mark occupied board position. Row= " +
                   Integer.toString(row) + " Column= " + Integer.toString(column));

        }

        checkBoardForWin(player);

        return;
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



    private void checkBoardForWin(Player player){

        List<Coordinate> positions = new ArrayList<>();
        // check verticals
        for (int column = 0; column < MAX_COLS; column++){
            positions.clear();
            int columnSum = 0;
            for (int row=0; row< MAX_ROWS; row++){
                Coordinate coordinate = new Coordinate(row, column);
                positions.add(coordinate);
                columnSum+= mBoard[row][column];
            }
            if (columnSum == (3 * player.value())){
                mWinLine = new WinLine(LineType.VERTICAL, positions);
               // winningLine = new WinLine(LineType.VERTICAL, 0, column);
                return;
            }
        }

        //check horizontals
        for (int row=0; row < MAX_ROWS; row++){
            positions.clear();
            int rowSum = 0;
            for (int column= 0; column< MAX_COLS; column++){
                Coordinate coordinate = new Coordinate(row, column);
                positions.add(coordinate);
                rowSum+= mBoard[row][column];
            }
            if (rowSum == (3 * player.value())){
                mWinLine= new WinLine(LineType.HORIZONTAL,positions);
                return;
            }
        }

        //check negative slope diagonal

        positions.clear();
        int diagonalSum = mBoard[2][0] + mBoard[1][1] + mBoard[0][2];
        if (diagonalSum == (3 * player.value())){
            positions.add(new Coordinate(2,0));
            positions.add(new Coordinate(1,1));
            positions.add(new Coordinate(0,2));
            mWinLine = new WinLine(LineType.DIAGONAL, positions);
            return;
        }

        //check positive diagonal
        positions.clear();
        diagonalSum = mBoard[0][0] + mBoard[1][1] + mBoard[2][2];
        if (diagonalSum == (3 * player.value())){
            positions.add(new Coordinate(0,0));
            positions.add(new Coordinate(1,1));
            positions.add(new Coordinate(2,2));
            mWinLine = new WinLine(LineType.DIAGONAL, positions);
            return;
        }

        return;
    }
}
