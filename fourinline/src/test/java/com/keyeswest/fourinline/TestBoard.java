package com.keyeswest.fourinline;

import com.keyeswest.core.Player;

public class TestBoard extends FourInLineBoard {

    public TestBoard(){
        super();
    }


    public void setPosition(int row, int column, int value){
        mBoard[row][column] = value;
    }


    public void constructHorizontalLine( int row,int startColumn, Player player){
        int endColumn = startColumn + FourInLineBoard.getWinConnection();
        if (endColumn >= getMAX_COLS()){
            endColumn = getMAX_COLS();
        }

        for (int col=startColumn; col< endColumn; col++ ){
            setPosition(row, col, player.value());
        }
    }

    public void constructVerticalLine(int startRow, int column, Player player){
        int endRow = startRow + FourInLineBoard.getWinConnection();
        if (endRow >= getMAX_ROWS()){
            endRow = getMAX_ROWS();
        }

        for(int row = startRow; row< endRow; row++){
            setPosition(row, column, player.value());
        }
    }

    public void constructPositiveDiagonal(int startRow, int startColumn, Player player){
        for(int index = 0; index< FourInLineBoard.getWinConnection(); index++){
            if ((startRow+index >=  getMAX_ROWS() ) || (startColumn+index > getMAX_COLS()) ){
                break;
            }
            setPosition(startRow+index, startColumn+index,player.value());
        }
    }

    public void constructNegativeDiagonal(int startRow, int startColumn, Player player){
        for(int index = 0; index< FourInLineBoard.getWinConnection(); index++){
            if ((startRow-index <0 ) || (startColumn+index > getMAX_COLS())  ){
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
        for(int rowIndex = 0; rowIndex< getMAX_ROWS(); rowIndex++){
            activePlayer = startPlayer;
           for(int colIndex = 0; colIndex < getMAX_COLS(); colIndex++){
               mBoard[rowIndex][colIndex] = activePlayer.value();
               activePlayer = activePlayer.getOpponent();
           }
        }

        // set the diagonal using the player value
        for (int index = 0; index< FourInLineBoard.getWinConnection(); index++){
            mBoard[row+index][column+index] = player.value();
        }

    }

}
