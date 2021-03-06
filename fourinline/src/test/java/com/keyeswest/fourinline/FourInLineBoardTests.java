package com.keyeswest.fourinline;

import com.keyeswest.core.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class FourInLineBoardTests {

    private FourInLineBoard mFourInLineBoard;

    @Before
    public void init(){
        mFourInLineBoard = new FourInLineBoard();
    }


    @Test
    public void availableMovesEmptyBoardTest(){
        List<FourInLineMove> moves = mFourInLineBoard.getAvailableMoves();
        Assert.assertNotNull(moves);
        Assert.assertEquals(moves.size(), mFourInLineBoard.getMAX_COLS() );
    }

    @Test
    public void availableMovesTest(){
        int expectedMoveCount = mFourInLineBoard.getMAX_COLS();
        List<FourInLineMove> moves;
        for (int column = 0; column< mFourInLineBoard.getMAX_COLS(); column++){
            moves = mFourInLineBoard.getAvailableMoves();
            Assert.assertNotNull(moves);
            Assert.assertEquals(moves.size(),expectedMoveCount );
            for(int row = 0; row< mFourInLineBoard.getMAX_ROWS(); row++){
                mFourInLineBoard.addPiece(Player.P1,new FourInLineMove(column));
            }
            expectedMoveCount--;

        }
    }

    @Test
    public void performValidMoveTest(){
        FourInLineMove move = new FourInLineMove(0);
        GameStatus result = mFourInLineBoard.performMove(move,Player.P1);

        Assert.assertTrue(result == GameStatus.IN_PROGRESS);
    }

    @Test(expected = IllegalStateException.class)
    public void performBogusMoveTest(){
        BogusMove move = new BogusMove();
        mFourInLineBoard.performMove(move,Player.P1);

    }

    @Test
    public void diagonalTestCaseTest(){
        // setup board with test case moves
        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(3));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(0));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(3));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(0));

        mFourInLineBoard.addPiece( Player.P1,  new FourInLineMove(4));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(0));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(0));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(1));

        mFourInLineBoard.addPiece( Player.P1,  new FourInLineMove(3));
        mFourInLineBoard.addPiece( Player.P2,  new FourInLineMove(3));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(1));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(2));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(1));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(0));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(4));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(1));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(4));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(4));

        mFourInLineBoard.addPiece( Player.P1,  new FourInLineMove(5));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(2));

        mFourInLineBoard.addPiece( Player.P1,  new FourInLineMove(5));
        mFourInLineBoard.addPiece( Player.P2,  new FourInLineMove(5));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(4));
        mFourInLineBoard.addPiece( Player.P2,  new FourInLineMove(6));

        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(5));
        mFourInLineBoard.addPiece( Player.P2, new FourInLineMove(2));


        // this is a winning negative diagonal move
        mFourInLineBoard.addPiece( Player.P1, new FourInLineMove(2));
        Assert.assertNotNull(mFourInLineBoard.getWinLine());
    }






    @Test(expected = IllegalStateException.class)
    public void invalidSmallColumnTest(){
        Player player = Player.P1;
        mFourInLineBoard.addPiece( player, new FourInLineMove(-1));
    }

    @Test(expected = IllegalStateException.class)
    public void invalidLargeColumnTest(){
        Player player = Player.P1;
        mFourInLineBoard.addPiece( player, new FourInLineMove(mFourInLineBoard.getMAX_COLS()));

    }




    @Test
    public void horizontalWinTest(){

        for(int testRow = 0; testRow< mFourInLineBoard.getMAX_ROWS(); testRow++) {
            for (int col = 0; col < mFourInLineBoard.getMAX_COLS(); col++) {
                TestBoard board = new TestBoard();
               // board.configureBoardForRowTests(testRow,Player.P1);
                board.constructHorizontalLine(testRow,col, Player.P1);

                board.checkBoardForWin(Player.P1, testRow, col);
                if (col <= board.getMAX_COLS() - FourInLineBoard.getWinConnection()) {
                    // horizontal line is within board bounds
                    Assert.assertNotNull(board.getWinLine());
                    Assert.assertEquals(LineType.HORIZONTAL,board.getWinLine().getLineType());
                } else {
                    Assert.assertNull(board.getWinLine());
                }
            }
        }
    }

    @Test
    public void verticalWinTest(){
        for(int testRow = 0; testRow< mFourInLineBoard.getMAX_ROWS(); testRow++) {
            for (int col = 0; col <mFourInLineBoard.getMAX_COLS(); col++) {
                TestBoard board = new TestBoard();
                board.constructVerticalLine(testRow, col, Player.P1);

                board.checkBoardForWin(Player.P1, testRow, col);
                if (testRow <= board.getMAX_ROWS() - TestBoard.getWinConnection()) {
                    // vertical line is within the board bounds
                    Assert.assertNotNull(board.getWinLine());
                    Assert.assertEquals(LineType.VERTICAL,board.getWinLine().getLineType());
                } else {
                    Assert.assertNull(board.getWinLine());
                }
            }
        }
    }


    // Test cases where a positive diagonal has 4 adjacent positions
    @Test
    public void positiveDiagonalWinTest(){

        // Test Case 1: Diagonal extending from row2,col1 to row5,col4
        int row = 2;
        int column = 1;
        TestBoard board = new TestBoard();
        board.constructPositiveDiagonal(row,column,Player.P1);
        board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(board.getWinLine());
        Assert.assertEquals(LineType.DIAGONAL,board.getWinLine().getLineType());

        // Test Case 2: Diagonal extending from row0,col3 to row3,col6
        row = 0;
        column = 3;
        board = new TestBoard();
        board.configureBoardForPositiveDiagonalTest(row,column,Player.P1);
        board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(board.getWinLine());
        Assert.assertEquals(LineType.DIAGONAL,board.getWinLine().getLineType());

    }

    @Test
    public void negativeDiagonalWinTest(){
        // Test Case 1:
        int row = 5;
        int column = 0;
        TestBoard board = new TestBoard();
        board.constructNegativeDiagonal(row,column,Player.P1);
        board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(board.getWinLine());
        Assert.assertEquals(LineType.DIAGONAL,board.getWinLine().getLineType());

        // Test Case 3:
        row = 3;
        column = 0;
        board = new TestBoard();
        board.constructNegativeDiagonal(row,column,Player.P1);
        board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(board.getWinLine());
        Assert.assertEquals(LineType.DIAGONAL,board.getWinLine().getLineType());

    }

    @Test
    public void positiveDiagonalConstructionTest(){
        // Test Case One:
        //   start of diagonal on row > 0
        //   end of diagonal on MAX ROW
        // text coordinate row=4, col=1
        int row=4;
        int column = 1;
        // expected diagonal segment startRow= 3 startCol=0, endRow=5 endCol=2
        int expectedStartRow= 3;
        int expectedStartColumn = 0;
        int expectedEndRow= 5;
        int expectedEndColumn = 2;
        LineSegment result  = mFourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);


        // Test Case Two:
        //   start of diagonal on row = 0
        //   end of diagonal on MAX ROW
        // text coordinate row=3, col=3
        row =3;
        column = 3;
        expectedStartRow= 0;
        expectedStartColumn = 0;
        expectedEndRow= 5;
        expectedEndColumn = 5;
        result  = mFourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        // Test Case Three:
        //   start of diagonal on row = 0
        //   end of diagonal on MAX COLUMN
        // text coordinate row=1, col=4
        row =1;
        column = 4;
        expectedStartRow= 0;
        expectedStartColumn = 3;
        expectedEndRow= 3;
        expectedEndColumn = 6;
        result  = mFourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        // Test Case Four:
        //  single point  = 0
        //
        row =5;
        column = 0;
        expectedStartRow= row;
        expectedStartColumn = column;
        expectedEndRow= row;
        expectedEndColumn = column;
        result  = mFourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        // Test Case Five:
        //
        row =1;
        column = 0;
        expectedStartRow= row;
        expectedStartColumn = column;
        expectedEndRow= 5;
        expectedEndColumn = 4;
        result  = mFourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

    }

    @Test
    public void negativeDiagonalConstructionTest(){
        //Test Case 1
        int row = 2;
        int column = 3;
        int expectedStartRow= 5;
        int expectedStartColumn = 0;
        int expectedEndRow= 0;
        int expectedEndColumn = 5;
        LineSegment result  = mFourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        //Test Case 2
        row = 2;
        column = 5;
        expectedStartRow= 5;
        expectedStartColumn = 2;
        expectedEndRow= 1;
        expectedEndColumn = 6;
        result  = mFourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        //Test Case 3
        row = 4;
        column = 5;
        expectedStartRow= 5;
        expectedStartColumn =4;
        expectedEndRow= 3;
        expectedEndColumn = 6;
        result  = mFourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);


        // Test Case 4:
        //  single point  = 0
        //
        row =0;
        column = 0;
        expectedStartRow= row;
        expectedStartColumn = column;
        expectedEndRow= row;
        expectedEndColumn = column;
        result  = mFourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

    }




    @Test
    public void playRandomGameTest(){
        int moveCount = 0;
        boolean gameOver = false;
        Player player = Player.P1;
        while (! gameOver){
            List<FourInLineMove> moves = mFourInLineBoard.getAvailableMoves();
            if (moves.isEmpty()){
                gameOver = true;
            }else {
                int range = moves.size();
                int randomSelection = (int) (Math.random() * range);

                FourInLineMove randomMove = moves.get(randomSelection);
                mFourInLineBoard.addPiece(player, new FourInLineMove(randomMove.getColumn()));

                moveCount++;
                Assert.assertTrue(moveCount<= (mFourInLineBoard.getMAX_ROWS() * mFourInLineBoard.getMAX_COLS()));

                if (mFourInLineBoard.getWinLine() != null) {
                    gameOver = true;
                }else{
                    player = player.getOpponent();
                }
            }

        }

    }



    private void checkLineCoordinates(int expectedStartRow, int expectedStartColumn,
                                      int expectedEndRow, int expectedEndColumn,
                                      LineSegment result){
        Assert.assertEquals(expectedStartRow, result.getStartRow());
        Assert.assertEquals(expectedStartColumn, result.getStartColumn());
        Assert.assertEquals(expectedEndRow, result.getEndRow());
        Assert.assertEquals(expectedEndColumn, result.getEndColumn());

    }

    public class BogusMove extends Move {

       public BogusMove(){
           super("Bogus");
       }
    }


}
