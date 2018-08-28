package com.keyeswest.tictactoe;

import com.keyeswest.core.Game;

import com.keyeswest.core.ManualPlayerCallback;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;
import com.keyeswest.tictactoe.view.Board;
import javafx.application.Platform;
import javafx.scene.Parent;

import java.util.Scanner;

public class TicTacToeGame extends Game {

    private final static String NAME="Tic-Tac-Toe";

    private Board mGraphicalDisplayBoard;

    private TicTacToeGame(TicTacToeGame game){
        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mGameState = game.mGameState.makeCopy();

    }

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public Parent getGraphicalBoardDisplay() {
        return mGraphicalDisplayBoard.createContent();
    }

    @Override
    public Game makeCopy() {
        return new TicTacToeGame(this);
    }

    @Override
    public Move getOpponentMove() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter P2 row move: ");
        int selectedRow = in.nextInt();
        in.nextLine();
        System.out.println("Enter P2 column move: ");
        System.out.flush();
        int selectedColumn = in.nextInt();
        in.nextLine();

        return new TicTacToeMove(selectedRow, selectedColumn);
    }

    public TicTacToeGame(Player initialPlayer, ManualPlayerCallback manualPlayerCallback){
        super(new TicTacToeBoard(), initialPlayer);
        mGraphicalDisplayBoard = new Board(manualPlayerCallback);
    }


    @Override
    public void setUserMessage(String message) {
        Platform.runLater(() -> mGraphicalDisplayBoard.setUserMessage(message));
    }

    @Override
    public void setManualPlayerTurn(boolean manualPlayerTurn) {
        Platform.runLater(() ->mGraphicalDisplayBoard.setManualPlayerTurn(manualPlayerTurn));
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
}
