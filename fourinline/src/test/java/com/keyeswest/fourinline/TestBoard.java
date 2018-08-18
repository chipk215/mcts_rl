package com.keyeswest.fourinline;

import com.keyeswest.core.Player;

public class TestBoard extends Board {

    public TestBoard(){
        super();
    }


    public void setPosition(int row, int column, int value){
        mBoard[row][column] = value;
    }


    public void constructHorizontalLine( int row,int startColumn, Player player){
        int endColumn = startColumn + Board.getWinConnection();
        if (endColumn >= Board.getMaxCols()){
            endColumn = Board.getMaxCols();
        }

        for (int col=startColumn; col< endColumn; col++ ){
            setPosition(row, col, player.value());
        }
    }

    public void constructVerticalLine(int startRow, int column, Player player){
        int endRow = startRow + Board.getWinConnection();
        if (endRow >= Board.getMaxRows()){
            endRow = Board.getMaxRows();
        }

        for(int row = startRow; row< endRow; row++){
            setPosition(row, column, player.value());
        }
    }

    public void constructPositiveDiagonal(int startRow, int startColumn, Player player){
        for(int index=0;index< Board.getWinConnection();index++){
            if ((startRow+index >=  Board.getMaxRows() ) || (startColumn+index > Board.getMaxCols()) ){
                break;
            }
            setPosition(startRow+index, startColumn+index,player.value());
        }
    }

    public void constructNegativeDiagonal(int startRow, int startColumn, Player player){
        for(int index=0;index< Board.getWinConnection();index++){
            if ((startRow-index <0 ) || (startColumn+index > Board.getMaxCols())  ){
                break;
            }
            setPosition(startRow-index, startColumn+index,player.value());
        }
    }

    public void configureBoardForPositiveDiagonalTest(int row, int column, Player player){
        Player  activePlayer;
        Player startPlayer = player;
        if ((column % 2) == 0){
            startPlayer = player.getOpponent();
        }

        // Fill the board alternating opponent and player positions across all rows
        for(int rowIndex=0; rowIndex< Board.getMaxRows(); rowIndex++){
            activePlayer = startPlayer;
           for(int colIndex =0; colIndex < Board.getMaxCols(); colIndex++){
               mBoard[rowIndex][colIndex] = activePlayer.value();
               activePlayer = activePlayer.getOpponent();
           }
        }

        // set the diagonal using the player value
        for (int index=0; index< Board.getWinConnection(); index++){
            mBoard[row+index][column+index] = player.value();
        }

    }

}
