package com.keyeswest.tictactoe.view;

import com.keyeswest.core.ManualPlayerCallback;
import com.keyeswest.core.Move;
import com.keyeswest.tictactoe.TicTacToeMove;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Board  implements CellClickHandler{

    private static double PANE_WIDTH = 600.0d;
    private static double PANE_HEIGHT = 400.0d;

    private final static String NEW_GAME_LABEL= "New Game";
    private final static String INITIALIZING_MESSAGE = "Initializing...";

    private Text mUserMessage;

    private BoardCell[][] mBoard;

    private Button mResetButton;

    private VBox mRoot;

    private ManualPlayerCallback mManualPlayerCallback;


    private boolean mManualPlayerTurn;

    public Board(ManualPlayerCallback manualPlayerCallback){
        mBoard = new BoardCell[3][3];
        mUserMessage = new Text();
        mManualPlayerTurn = false;
        mManualPlayerCallback = manualPlayerCallback;
    }

    public Parent createContent(){
        mRoot = new VBox();
        mRoot.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        mRoot.setPadding(new Insets(15));
        mRoot.setSpacing(10);

        mUserMessage.setFont(Font.font(18));
        mUserMessage.setTextOrigin(VPos.TOP);
        mUserMessage.setText(INITIALIZING_MESSAGE);

        Pane boardPane = new Pane();

        boardPane.setPrefSize(275,275);
        boardPane.setPadding(new Insets(25));
        for (int row=0; row<3; row++){
            for(int col=0; col< 3; col++) {

                BoardCell cell = new BoardCell(2-row, col, this);
                cell.setTranslateY( row * 75);
                cell.setTranslateX(175 +col * 75);
                boardPane.getChildren().add(cell);

                mBoard[2-row][col] = cell;
            }
        }

        mResetButton = new Button(NEW_GAME_LABEL);
        mRoot.getChildren().addAll( mUserMessage,boardPane,mResetButton);
        mRoot.setAlignment(Pos.TOP_CENTER);

        return mRoot;
    }

    public void setUserMessage(String message){
        mUserMessage.setText(message);
    }

    public void setManualPlayerTurn(boolean manualPlayerTurn){
        mManualPlayerTurn = manualPlayerTurn;
    }

    public void markCell(int row, int column, boolean manualPlayer){
        if (manualPlayer){
            mBoard[row][column].drawO();
        }else{
            mBoard[row][column].drawX();
        }
    }

    @Override
    public void onCellClicked(BoardCell cell) {
        if ((!mManualPlayerTurn) || (cell.isMarked())) {
            // ignore cell clicks if not the manual user's turn or if cell is already marked

            //TODO investigate disabling mouse clicks for marked cells
            return;
        }

        Move playerMove = new TicTacToeMove(cell.getRow(), cell.getColumn());
        mManualPlayerCallback.opponentMove(playerMove);
        // handle click event for cell
    }
}