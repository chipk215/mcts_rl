package com.keyeswest.tictactoe;

import com.keyeswest.core.*;

import com.keyeswest.tictactoe.view.Board;
import javafx.application.Platform;
import javafx.scene.Parent;


public class TicTacToeGame extends Game {

    private final static String NAME="Tic-Tac-Toe";

    private Board mGraphicalDisplayBoard;


    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public Parent getGraphicalBoardDisplay() {
        return mGraphicalDisplayBoard.createContent();
    }

    public TicTacToeGame(Player initialPlayer, GameCallback gameCallback){
        super(new GameState(new TicTacToeBoard(),initialPlayer, GameStatus.IN_PROGRESS, null));

        mGraphicalDisplayBoard = new Board(gameCallback);
    }

    @Override
    public void setUserMessage(UserMessages message) {
        Platform.runLater(() -> mGraphicalDisplayBoard.setUserMessage(USER_MESSAGES.get(message)));
    }

    @Override
    public void setManualPlayerTurn(boolean manualPlayerTurn) {
        mGraphicalDisplayBoard.setManualPlayerTurn(manualPlayerTurn);
    }

    @Override
    public void displayMove(Move move, Player player) {
        if (!(move instanceof TicTacToeMove)) {
            throw new IllegalStateException("Unrecognized game move.");
        }
        int row = ((TicTacToeMove)move).getRow();
        int column = ((TicTacToeMove)move).getColumn();
        boolean manualPlayer = true;
        // Assume P1 is always computer -- verify this logic
        if (player == Player.P1){
            manualPlayer = false;

        }

        boolean finalManualPlayer = manualPlayer;
        Platform.runLater(() -> mGraphicalDisplayBoard.markCell(row, column, finalManualPlayer));
    }

    @Override
    public void showWinner() {
       Platform.runLater(() -> mGraphicalDisplayBoard.showWinLine(mGameState.getWinLine().getWinningPositions()));
    }
}
