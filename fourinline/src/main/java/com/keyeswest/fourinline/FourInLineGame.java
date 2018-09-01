package com.keyeswest.fourinline;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.view.Board;
import javafx.scene.Parent;

import java.util.List;
import java.util.Scanner;

public class FourInLineGame extends Game {

    private final static String NAME="4 In A Line!";

    private Board mGraphicalDisplayBoard;


    public FourInLineGame(Player initialPlayer, GameCallback gameCallback){
        super(new GameState(new FourInLineBoard(),initialPlayer, GameStatus.IN_PROGRESS, null));

        mGraphicalDisplayBoard = new Board();
    }

    @Override
    public Parent getGraphicalBoardDisplay() {

        return mGraphicalDisplayBoard.createContent();
    }



    @Override
    public String getName(){
        return NAME;
    }


    public Move getOpponentMove() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter P2 move: ");
        System.out.flush();
        int selectedColumn = in.nextInt();
        in.nextLine();

        return new FourInLineMove(selectedColumn);
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
