package com.keyeswest.fourinline;

import com.keyeswest.core.Move;
import com.keyeswest.core.MoveStatus;
import com.keyeswest.core.Player;
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
    public void createEmptyBoardTest(){

        List<FourInLineBoard.CellOccupant> occupants = mFourInLineBoard.getBoardPositions();
        Assert.assertTrue(occupants.isEmpty());
    }

    @Test
    public void availableMovesEmptyBoardTest(){
        List<FourInLineMove> moves = mFourInLineBoard.getAvailableMoves();
        Assert.assertNotNull(moves);
        Assert.assertEquals(moves.size(), FourInLineBoard.getMaxCols() );
    }

    @Test
    public void availableMovesTest(){
        int expectedMoveCount = FourInLineBoard.getMaxCols();
        List<FourInLineMove> moves;
        for (int column = 0; column< FourInLineBoard.getMaxCols(); column++){
            moves = mFourInLineBoard.getAvailableMoves();
            Assert.assertNotNull(moves);
            Assert.assertEquals(moves.size(),expectedMoveCount );
            for(int row = 0; row< FourInLineBoard.getMaxRows(); row++){
                mFourInLineBoard.addPiece(Player.P1,column);
            }
            expectedMoveCount--;

        }
    }

    @Test
    public void performValidMoveTest(){
        FourInLineMove move = new FourInLineMove(0);
        MoveStatus result = mFourInLineBoard.performMove(Player.P1, move);

        Assert.assertTrue(result.isValid());
    }

    @Test
    public void performBogusMoveTest(){
        BogusMove move = new BogusMove();
        MoveStatus result  = mFourInLineBoard.performMove(Player.P1, move);
        Assert.assertFalse(result.isValid());
    }



    @Test
    public void validMoveTest(){
        Player player = Player.P1;
        int expectedPosition = 0;
        FourInLineMoveStatus status = mFourInLineBoard.addPiece( player, 0);
        Assert.assertTrue(status.isValid());
        List<FourInLineBoard.CellOccupant> occupants = mFourInLineBoard.getBoardPositions();
        Assert.assertTrue(occupants.size() == 1);
        FourInLineBoard.CellOccupant occupant = occupants.get(0);
        Assert.assertEquals(player, occupant.getPlayer());
        Assert.assertEquals(expectedPosition, occupant.getCellNumber());

    }


    @Test
    public void invalidColumnTest(){
        Player player = Player.P1;
        FourInLineMoveStatus status = mFourInLineBoard.addPiece( player, -1);
        Assert.assertFalse(status.isValid());
        status = mFourInLineBoard.addPiece( player, FourInLineBoard.getMaxCols());
        Assert.assertFalse(status.isValid());

    }


    // Verify that cell can be occupied above an occupied cell
    @Test
    public void validStackTest(){
        int expectedPlayerOnePosition = 0;
        int expectedPlayerTwoPosition = FourInLineBoard.getMaxCols();
        Player playerOne = Player.P1;
        Player playerTwo = Player.P2;
        FourInLineMoveStatus status = mFourInLineBoard.addPiece( playerOne, 0);
        Assert.assertTrue(status.isValid());
        status = mFourInLineBoard.addPiece( playerTwo, 0);
        Assert.assertTrue(status.isValid());
        List<FourInLineBoard.CellOccupant> occupants = mFourInLineBoard.getBoardPositions();
        Assert.assertTrue(occupants.size() == 2);

        for (int i=0; i< 2; i++) {
            FourInLineBoard.CellOccupant occupant = occupants.get(i);
            if (occupant.getPlayer() == playerOne) {
               Assert.assertEquals(expectedPlayerOnePosition,occupant.getCellNumber() );
            }else{
                Assert.assertEquals(expectedPlayerTwoPosition,occupant.getCellNumber() );
            }
        }

    }

    @Test
    public void horizontalWinTest(){

        for(int testRow = 0; testRow< FourInLineBoard.getMaxRows(); testRow++) {
            for (int col = 0; col < FourInLineBoard.getMaxCols(); col++) {
                TestBoard board = new TestBoard();
               // board.configureBoardForRowTests(testRow,Player.P1);
                board.constructHorizontalLine(testRow,col, Player.P1);

                WinLine result = board.checkBoardForWin(Player.P1, testRow, col);
                if (col <= FourInLineBoard.getMaxCols() - FourInLineBoard.getWinConnection()) {
                    // horizontal line is within board bounds
                    Assert.assertNotNull(result);
                    Assert.assertEquals(LineType.HORIZONTAL,result.getLineType());
                } else {
                    Assert.assertNull(result);
                }
            }
        }
    }

    @Test
    public void verticalWinTest(){
        for(int testRow = 0; testRow< FourInLineBoard.getMaxRows(); testRow++) {
            for (int col = 0; col < FourInLineBoard.getMaxCols(); col++) {
                TestBoard board = new TestBoard();
                board.constructVerticalLine(testRow, col, Player.P1);

                WinLine result = board.checkBoardForWin(Player.P1, testRow, col);
                if (testRow <= FourInLineBoard.getMaxRows() - FourInLineBoard.getWinConnection()) {
                    // vertical line is within the board bounds
                    Assert.assertNotNull(result);
                    Assert.assertEquals(LineType.VERTICAL,result.getLineType());
                } else {
                    Assert.assertNull(result);
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
        WinLine result = board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(result);
        Assert.assertEquals(LineType.DIAGONAL,result.getLineType());

        // Test Case 2: Diagonal extending from row0,col3 to row3,col6
        row = 0;
        column = 3;
        board = new TestBoard();
        board.configureBoardForPositiveDiagonalTest(row,column,Player.P1);
        result = board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(result);
        Assert.assertEquals(LineType.DIAGONAL,result.getLineType());

    }

    @Test
    public void negativeDiagonalWinTest(){
        // Test Case 1:
        int row = 5;
        int column = 0;
        TestBoard board = new TestBoard();
        board.constructNegativeDiagonal(row,column,Player.P1);
        WinLine result = board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(result);
        Assert.assertEquals(LineType.DIAGONAL,result.getLineType());

        // Test Case 3:
        row = 3;
        column = 0;
        board = new TestBoard();
        board.constructNegativeDiagonal(row,column,Player.P1);
        result = board.checkBoardForWin(Player.P1, row, column);
        Assert.assertNotNull(result);
        Assert.assertEquals(LineType.DIAGONAL,result.getLineType());

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
        LineSegment result  = FourInLineBoard.computePositiveDiagonalSegment(row,column);
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
        result  = FourInLineBoard.computePositiveDiagonalSegment(row,column);
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
        result  = FourInLineBoard.computePositiveDiagonalSegment(row,column);
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
        result  = FourInLineBoard.computePositiveDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        // Test Case Five:
        //
        row =1;
        column = 0;
        expectedStartRow= row;
        expectedStartColumn = column;
        expectedEndRow= 5;
        expectedEndColumn = 4;
        result  = FourInLineBoard.computePositiveDiagonalSegment(row,column);
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
        LineSegment result  = FourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        //Test Case 2
        row = 2;
        column = 5;
        expectedStartRow= 5;
        expectedStartColumn = 2;
        expectedEndRow= 1;
        expectedEndColumn = 6;
        result  = FourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

        //Test Case 3
        row = 4;
        column = 5;
        expectedStartRow= 5;
        expectedStartColumn =4;
        expectedEndRow= 3;
        expectedEndColumn = 6;
        result  = FourInLineBoard.computeNegativeDiagonalSegment(row,column);
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
        result  = FourInLineBoard.computeNegativeDiagonalSegment(row,column);
        checkLineCoordinates(expectedStartRow, expectedStartColumn, expectedEndRow, expectedEndColumn,result);

    }


    @Test
    public void getBoardPositionsTest(){


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
                FourInLineMoveStatus status = mFourInLineBoard.addPiece(player, randomMove.getColumn());
                Assert.assertTrue(status.isValid());
                moveCount++;
                Assert.assertTrue(moveCount<= (FourInLineBoard.getMaxRows() * FourInLineBoard.getMaxCols()));
                WinLine winner = mFourInLineBoard.checkBoardForWin(player, status.getRow(), status.getColumn());
                if (winner != null) {
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

    }


}
