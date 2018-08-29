package com.keyeswest.fourinline;

import com.keyeswest.core.*;
import javafx.scene.Parent;

import java.util.List;
import java.util.Scanner;

public class FourInLineGame extends Game {

    private final static String NAME="4 In A Line!";

    private FourInLineGame(FourInLineGame game){
        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mGameState = game.mGameState.makeCopy();
    }

    @Override
    public Parent getGraphicalBoardDisplay() {
        return null;
    }

    @Override
    public Game makeCopy() {
        return new FourInLineGame(this);
    }

    @Override
    public String getName(){
        return NAME;
    }

    @Override
    public Move getOpponentMove() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter P2 move: ");
        System.out.flush();
        int selectedColumn = in.nextInt();
        in.nextLine();

        return new FourInLineMove(selectedColumn);
    }

    public FourInLineGame(GameBoard board, Player initialPlayer ){
        super(board, initialPlayer);
    }

    @Override
    public void setUserMessage(UserMessages message) {

    }

    @Override
    public void setManualPlayerTurn(boolean manualPlayerTurn) {

    }

    @Override
    public void displayMove(Move move, Player player) {

    }

    @Override
    public void showWinner() {

    }
}
