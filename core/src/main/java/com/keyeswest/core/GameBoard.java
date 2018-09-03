package com.keyeswest.core;


import java.util.List;
import java.util.logging.Logger;

public abstract class GameBoard {

    protected static final Logger LOGGER = Logger.getLogger(GameBoard.class.getName());

    protected static int EMPTY = 0;

    protected final int MAX_ROWS;
    protected final int MAX_COLS;

    protected WinLine mWinLine = null;

    // index (0,0) corresponds to bottom left of board
    protected int[] [] mBoard;


    protected GameBoard(int rows,int columns){
        MAX_ROWS = rows;
        MAX_COLS = columns;

        mBoard = new int[MAX_ROWS][MAX_COLS];
    }

    protected GameBoard(GameBoard board){
        MAX_ROWS = board.MAX_ROWS;
        MAX_COLS = board.MAX_COLS;
        mBoard = new int[MAX_ROWS][MAX_COLS];
        for (int row=0; row< MAX_ROWS; row++){
            for (int col=0; col< MAX_COLS; col++){
                mBoard[row][col] = board.mBoard[row][col];
            }
        }

        if (board.mWinLine != null){
            mWinLine = new WinLine(board.mWinLine);
        }

    }

    // Note: This method may require a player for some games.
    public abstract List<? extends Move> getAvailableMoves();

    public abstract GameStatus performMove(Move move,Player player);

    public void logBoardPositions(Logger clientLogger){
        Logger logger = clientLogger;
        if (logger == null){
            logger = LOGGER;
        }

        StringBuilder sBuilder = new StringBuilder(System.lineSeparator());
        for (int row=MAX_ROWS-1; row>=0; row--){
            for (int column=0; column< MAX_COLS; column++){
                if (mBoard[row][column] == Player.P1.value()){
                    sBuilder.append("[X]  ");
                }else if(mBoard[row][column] == Player.P2.value()){
                    sBuilder.append("[O]  ");
                }else{
                    sBuilder.append("[ ]  ");
                }
            }
            sBuilder.append(System.lineSeparator());
            //System.out.println();
        }
        logger.info(sBuilder.toString());
    }


    public WinLine getWinLine() {
        if (mWinLine == null){
            return null;
        }
        return new WinLine(mWinLine);
    }

    public int getMAX_ROWS() {
        return MAX_ROWS;
    }

    public int getMAX_COLS() {
        return MAX_COLS;
    }

    public abstract GameBoard makeCopy();


}
