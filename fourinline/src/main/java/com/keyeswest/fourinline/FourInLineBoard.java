package com.keyeswest.fourinline;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.keyeswest.core.*;

/**
 * Iteration Analysis
 * <p>
 * Since there a 7 columns on the board, there are 7 possible moves or actions that can be taken
 * by a player until a column becomes filled.
 * <p>
 * From the empty board (level 1) state there are 7 possible moves.  To fully expand the 2nd level of the search tree
 * requires 7 + 7^2 = 56 moves.
 * <p>
 * Third level expansion requires an additional 7^2 iteration = 7 + 7^2 + 7^2
 * <p>
 * Generally for the nth level 7^n + 7^(n-1) + 7^(n-2) + ... + 7^1
 * <p>
 * For reference:
 * n       7^n       total iterations
 * 1         7               7
 * 2        49              56
 * 3       343             399
 * 4     2,401           2,800
 * 5    16,807          19,607
 * 6
 * <p>
 * 3000 iterations looks ahead past 4 moves but doesn't fully expand the 5th level.
 **/

public class FourInLineBoard extends GameBoard {

    private static int MAXIMUM_ROWS = 6;
    private static int MAXIMUM_COLS = 7;
    private static int WIN_CONNECTION = 4;

    static int getWinConnection() {
        return WIN_CONNECTION;
    }


    public FourInLineBoard() {
        super(MAXIMUM_ROWS, MAXIMUM_COLS);
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int col = 0; col < MAX_COLS; col++) {
                mBoard[row][col] = EMPTY;
            }
        }
    }

    private FourInLineBoard(FourInLineBoard board) {
        super(board);

    }

    @Override
    public List<FourInLineMove> getAvailableMoves() {
        List<FourInLineMove> moves = new ArrayList<>();
        for (int column = 0; column < MAX_COLS; column++) {
            for (int row = 0; row < MAX_ROWS; row++) {
                if (mBoard[row][column] == EMPTY) {
                    moves.add(new FourInLineMove(column));
                    break;
                }
            }
        }
        return moves;
    }



    @Override
    public GameStatus performMove(Move move, Player player) {

        addPiece(player, move);
        GameStatus gameStatus = GameStatus.IN_PROGRESS;
        if (mWinLine != null) {
            gameStatus = GameStatus.GAME_WON;
        } else {
            // check for tie
            if (getAvailableMoves().size() == 0) {
                gameStatus = GameStatus.GAME_TIED;
            }
        }
        return gameStatus;

    }

    @Override
    public GameBoard makeCopy() {
        return new FourInLineBoard(this);
    }

    private void logWinMessage(WinLine winner, Player player) {
        LOGGER.info("***** Game won by player: " + player.toString());
        LOGGER.info("Win line type: " + winner.getLineType().toString());
    }


    void addPiece(Player player, Move move) {

        if (!(move instanceof FourInLineMove)) {
            LOGGER.log(Level.SEVERE, "Unable to downcast game move.");
            throw new IllegalStateException("Unrecognized game move.");
        }

        int column = ((FourInLineMove) move).getColumn();
        if ((column < 0) || (column >= MAX_COLS)) {
            throw new IllegalStateException("Illegal and unexpected game move executed.");
        }
        int row = getNextAvailableRow(column);
        if (row == -1) {
            throw new IllegalStateException("Illegal and unexpected game move executed.");
        }
        ((FourInLineMove) move).setRow(row);
        mBoard[row][column] = player.value();


        checkBoardForWin(player, row, column);

    }

    /**
     * Check board for win based upon the player's last move. This algorithm requires this
     * method to be invoked after every move, or at least after every move
     * after the player has made 4 (minimum) number of moves to form a winning line.
     *
     * @param player - player making move
     * @param row    - row containing the last move
     * @param column - column containing last move
     */
    void checkBoardForWin(Player player, int row, int column) {

        checkHorizontalsForWin(player, row, column);
        if (mWinLine != null) {
            return;
        }

        checkVerticalsForWin(player, row, column);
        if (mWinLine != null) {
            return;
        }

        checkDiagonalsForWin(player, row, column);

        return;
    }


    private int getNextAvailableRow(int column) {
        for (int row = 0; row < MAX_ROWS; row++) {
            if (mBoard[row][column] == 0) {
                return row;
            }
        }
        // if no slot in the column is available return -1
        return -1;
    }



    /*
     * Check across the vertical column containing the coordinate provided
     * in the parameter list for consecutive positions corresponding to the
     * specified player.
     */
    private void checkVerticalsForWin(Player player, int row, int column) {
        List<Coordinate> positions = new ArrayList<>();
        // check segments beginning 3 rows beneath the specified row
        int startRow = row - (WIN_CONNECTION - 1);
        if (startRow < 0) {
            startRow = 0;
        }
        int matchSum = getMatchSum(player);

        while ((startRow <= row) && (startRow + WIN_CONNECTION <= MAX_ROWS)) {
            positions.clear();
            int sum = 0;
            for (int i = startRow; i < startRow + WIN_CONNECTION; i++) {
                sum += mBoard[i][column];
                positions.add(new Coordinate(i,column));
                if ((sum == 0) || (sum == player.getOpponent().value())) {
                    break;
                }
            }
            if (sum == matchSum) {
                // we have a winner
                mWinLine = new WinLine(LineType.VERTICAL, positions);
                return;
            }
            startRow++;
        }

    }


    /*
     * Check across the horizontal row containing the coordinate provided
     * in the parameter list for consecutive positions corresponding to the
     * specified player.
     */
    private void checkHorizontalsForWin(Player player, int row, int column) {
        List<Coordinate> positions = new ArrayList<>();
        // need to check at most 4 segments beginning 3 cols to the left of the
        // provided coordinate
        int startCol = column - (WIN_CONNECTION - 1);
        if (startCol < 0) {
            startCol = 0;
        }
        int matchSum = getMatchSum(player);

        while ((startCol <= column) && (startCol + WIN_CONNECTION <= MAX_COLS)) {
            positions.clear();
            int sum = 0;
            for (int i = startCol; i < startCol + WIN_CONNECTION; i++) {
                sum += mBoard[row][i];
                positions.add(new Coordinate(row,i));
                if ((sum == 0) || (sum == player.getOpponent().value())) {
                    break;
                }
            }
            if (sum == matchSum) {
                // we have a winner
                mWinLine = new WinLine(LineType.HORIZONTAL, positions);
                return;
            }

            startCol++;
        }

    }


    /*
     * Helper function for checking diagonals that looks for adjacent positions.
     */
    private void checkAdjacentPositions(int rowIndex, int colIndex, Player player, boolean negativeSlope) {
        List<Coordinate> positions = new ArrayList<>();
        int sum = 0;
        int matchSum = 4 * player.value();
        for (int index = 0; index < WIN_CONNECTION; index++) {
            if (negativeSlope) {
                sum += mBoard[rowIndex - index][colIndex + index];
                positions.add(new Coordinate(rowIndex-index,colIndex+index));
            } else {
                sum += mBoard[rowIndex + index][colIndex + index];
                positions.add(new Coordinate(rowIndex+index,colIndex+index));
            }
            if ((sum == 0) || (sum == player.getOpponent().value())) {
                break;
            }
        }
        if (sum == matchSum) {
            mWinLine = new WinLine(LineType.DIAGONAL, positions);
            return;
        }

    }

    /*
     * Compute the diagonals that intersect the coordinate provided in the
     * parameters.  Check the diagonal for 4 adjacent positions corresponding
     * to the specified player.
     */
    private void checkDiagonalsForWin(Player player, int row, int column) {

        LineSegment positiveDiagonal = computePositiveDiagonalSegment(row, column);
        if (positiveDiagonal.getLength() >= WIN_CONNECTION) {
            // check for 4 adjacent positions along diagonal
            int rowIndex = positiveDiagonal.getStartRow();
            int colIndex = positiveDiagonal.getStartColumn();
            while ((rowIndex <= positiveDiagonal.getEndRow() - WIN_CONNECTION + 1) &&
                    (colIndex <= positiveDiagonal.getEndColumn() - WIN_CONNECTION + 1)) {
                checkAdjacentPositions(rowIndex, colIndex, player, false);
                if (mWinLine != null) {
                    return;
                }
                rowIndex++;
                colIndex++;
            }
        }
        // check negativeDiagonal
        LineSegment negativeDiagonal = computeNegativeDiagonalSegment(row, column);
        if (negativeDiagonal.getLength() >= WIN_CONNECTION) {
            // check for 4 adjacent positions along diagonal
            int rowIndex = negativeDiagonal.getStartRow();
            int colIndex = negativeDiagonal.getStartColumn();
            while ((rowIndex >= negativeDiagonal.getEndRow() + WIN_CONNECTION - 1) &&
                    (colIndex <= negativeDiagonal.getEndColumn() - WIN_CONNECTION + 1)) {

                checkAdjacentPositions(rowIndex, colIndex, player, true);
                if (mWinLine != null) {
                    return;
                }
                rowIndex--;
                colIndex++;
            }
        }

    }


    private static int computeStartRowForPositiveDiagonal(int row, int column) {
        return Math.max(0, row - column);
    }

    private int computeStartRowForNegativeDiagonal(int row, int column) {
        return Math.min(row + column, MAX_ROWS - 1);
    }

    private static int computeStartColumnForPositiveDiagonal(int row, int column) {
        return Math.max(0, column - row);
    }

    private int computeStartColumnForNegativeDiagonal(int row, int column) {
        return Math.max(0, row + column - MAX_ROWS + 1);
    }


    private int getMatchSum(Player player) {
        return WIN_CONNECTION * player.value();
    }

    /**
     * Compute the start coordinate and end coordinate for positive sloping diagonals
     * that intersect the point provided as a parameter. The diagonal extends across
     * the board extending from either the bottom or left edge and ending at either
     * the top or right edge.
     * <p>
     * Here is the table corresponding to the 12 possible diagonals:
     * <p>
     * Row Start     Col Start      Row End    Col End
     * 0             0              5          5
     * 1             0              5          4
     * 2             0              5          3
     * 3             0              5          2
     * 4             0              5          1
     * 5             0              5          0
     * 0             1              5          6
     * 0             2              4          6
     * 0             3              3          6
     * 0             4              2          6
     * 0             5              1          6
     * 0             6              0          6
     *
     * @param row    row coordinate of intersecting point
     * @param column column coordinate of intersecting point
     * @return positive diagonal which intersects specified point
     */
    LineSegment computePositiveDiagonalSegment(int row, int column) {
        int endRow, endColumn;
        //Process the positive slope diagonal going through the point
        // - the start of the positive diagonal is:
        //     start row = max(0, row-col)
        //     start col = max(0, col-row)
        int startRow = computeStartRowForPositiveDiagonal(row, column);
        int startColumn = computeStartColumnForPositiveDiagonal(row, column);

        endRow = startColumn == 0 ? MAX_ROWS - 1 : MAX_ROWS - startColumn;

        endColumn = startColumn != 0 ? MAX_COLS - 1 : MAX_COLS - startRow - 2;

        return new LineSegment(startRow, startColumn, endRow, endColumn);
    }

    /**
     * Similar to computePositiveDiagonalSegment except the the computed diagonal has
     * a negative slope.
     *
     * @param row    row coordinate of intersecting point
     * @param column column coordinate of intersecting point
     * @return negative diagonal which intersects specified point
     */
    LineSegment computeNegativeDiagonalSegment(int row, int column) {
        //Process the negative slope diagonal going through the point

        int startRow = computeStartRowForNegativeDiagonal(row, column);
        int startColumn = computeStartColumnForNegativeDiagonal(row, column);
        int endRow = Math.max(0, startColumn - 1);
        int endColumn = Math.min(MAX_COLS - 1, startRow + startColumn);

        return new LineSegment(startRow, startColumn, endRow, endColumn);
    }


}
