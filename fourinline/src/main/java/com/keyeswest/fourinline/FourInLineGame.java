package com.keyeswest.fourinline;

import com.keyeswest.core.*;
import com.keyeswest.fourinline.view.Board;
import javafx.application.Platform;
import javafx.scene.Parent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FourInLineGame extends Game {

    private static final String YOUR_TURN_MESSAGE = "Your (Blue) turn to make a move.";


    @SuppressWarnings("Duplicates")
    private static final Map<UserMessages,String> USER_MESSAGES = new HashMap<UserMessages, String>(){{
        put(UserMessages.THINKING,THINK_MESSAGE);
        put(UserMessages.YOUR_TURN,YOUR_TURN_MESSAGE);
        put(UserMessages.TIED,TIED_MESSAGE);
        put(UserMessages.OPPONENT_WIN, OPPONENT_WIN_MESSAGE);
        put(UserMessages.COMPUTER_WIN,COMPUTER_WIN_MESSAGE);
    }};

    private final static String NAME="4 In A Line!";

    private Board mGraphicalDisplayBoard;


    public FourInLineGame(Player initialPlayer, GameCallback gameCallback){
        super(new GameState(new FourInLineBoard(),initialPlayer, GameStatus.IN_PROGRESS, null));

        mGraphicalDisplayBoard = new Board(gameCallback);
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
        Platform.runLater(() -> mGraphicalDisplayBoard.setUserMessage(USER_MESSAGES.get(message)));
    }

    @Override
    public void setManualPlayerTurn(boolean manualPlayerTurn) {
        Platform.runLater(() ->mGraphicalDisplayBoard.setManualPlayerTurn(manualPlayerTurn));
    }

    @Override
    public void displayMove(Move move, Player player) {
        if (!(move instanceof FourInLineMove)) {
            throw new IllegalStateException("Unrecognized game move.");
        }
        int row = ((FourInLineMove)move).getRow();
        int column = ((FourInLineMove)move).getColumn();

        // Assume P1 is always computer
        if (player == Player.P1){
            Platform.runLater(() -> mGraphicalDisplayBoard.showComputerMove(row, column));
        }




    }

    @Override
    public void showWinner() {

    }
}
