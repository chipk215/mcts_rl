package com.keyeswest.fourinline.view;

import com.keyeswest.core.Coordinate;
import com.keyeswest.core.GameCallback;
import com.keyeswest.core.MoveValue;
import com.keyeswest.fourinline.FourInLineMove;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;
import java.util.logging.Logger;

public class Board {

    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());
    private static double PANE_WIDTH = 440;
    private static double PANE_HEIGHT = 600;
    private static int NUM_ROWS = 6;
    private static int NUM_COLS = 7;

    private final static String NEW_GAME_LABEL = "New Game";
    private final static String INITIALIZING_MESSAGE = "Initializing...";

    private VBox mRoot;
    private Text mUserMessage;
    private Button mResetButton;

    private Pane mBoard;

    private GameCallback mGameCallback;
    private StackPane[][] mGrid;
    private Circle[][] mCircles;
    private Boolean[][] mGridFill;

    private Label[] mMoveValues;

    private boolean mManualPlayerTurn;

    private Circle mSelectionBall;

    public Board(GameCallback gameCallback){
        mUserMessage = new Text();
        mGameCallback = gameCallback;
        mGrid = new StackPane[NUM_ROWS][NUM_COLS];
        mGridFill = new Boolean[NUM_ROWS][NUM_COLS];
        mMoveValues = new Label[NUM_COLS];
        mCircles = new Circle[NUM_ROWS][NUM_COLS];
    }


    public Parent createContent(){
        mRoot = new VBox();
        mRoot.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        mRoot.setPadding(new Insets(15));
        mRoot.setSpacing(10);

        mUserMessage.setFont(Font.font(18));
        mUserMessage.setTextOrigin(VPos.TOP);
        mUserMessage.setText(INITIALIZING_MESSAGE);

        Image cell = new Image("file:fourinline/images/container.png");


        mBoard = new Pane();
        mBoard.setPrefSize(400,400);
        mBoard.setPadding(new Insets(25));


        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                ImageView cellView = new ImageView(cell);
                mGrid[row][col] = new StackPane();
                mGrid[row][col].getChildren().add(cellView);
                mGrid[row][col].relocate(25 + (col * 50), 75 + (row * 50));
                mBoard.getChildren().add(mGrid[row][col]);
                mGridFill[row][col] = false;
            }
        }

        for (int col=0; col< NUM_COLS; col++){
            Label label = new Label("");
            label.setTranslateX(35 + col*50);
            label.setFont(Font.font(10));
            label.setTranslateY(380);
            mBoard.getChildren().add(label);
            mMoveValues[col] = label;
        }


        mBoard.setOnMouseMoved(event -> {
            int lastBallX=0;

            if (mManualPlayerTurn){
                mBoard.setOnMouseClicked(clickEvent ->{

                    int ballColumn = computeColumn(mSelectionBall.getLayoutX());
                    if ( ! mGridFill[0][ballColumn]){
                        for(int row=NUM_ROWS-1; row>=0; row--){
                            if(! mGridFill[row][ballColumn]){
                                mGridFill[row][ballColumn] = true;
                                LOGGER.info("ball row" + Integer.toString(row));
                                LOGGER.info("ball col= " + Integer.toString(ballColumn));

                                mSelectionBall.relocate(25 + ballColumn*50,25+ (row+1)*50);
                                mCircles[row][ballColumn] = mSelectionBall;
                                break;
                            }
                        }
                        mManualPlayerTurn = false;
                        mGameCallback.opponentMove(new FourInLineMove(ballColumn));
                    }
                });

                // Move ball across the columns
                double mouseX =event.getX();
                double ballX = mSelectionBall.getLayoutX();
                //LOGGER.info("ballX= " + Double.toString(ballX));
                int ballColumn = computeColumn(ballX) +1;

                int mouseColumn = computeColumn(mouseX)+1;
                if (mouseColumn != ballColumn) {
                    lastBallX = 25+(mouseColumn-1)*50;
                    mSelectionBall.relocate(lastBallX,25);
                }
            }
        });

        mResetButton = new Button(NEW_GAME_LABEL);
        mResetButton.setOnAction(event -> mGameCallback.resetGame());
        mRoot.getChildren().addAll(mUserMessage, mBoard, mResetButton);
        mRoot.setAlignment(Pos.TOP_CENTER);

        return mRoot;
    }


    public void setUserMessage(String message) {
        mUserMessage.setText(message);
    }

    private void addCandidateValueLabels(List<MoveValue> candidates){
        for (int col =0; col < NUM_COLS; col++){
            mMoveValues[col].setText("");
        }
        if (candidates != null){
            for (MoveValue mv : candidates){
                FourInLineMove move = (FourInLineMove)mv.getMove();
                double moveValue = mv.getValue();
                mMoveValues[move.getColumn()].setText(Double.toString(moveValue));
            }
        }
    }

    public void showComputerMove(int row, int column, List<MoveValue> candidates){

        mGridFill[NUM_ROWS-row-1][column] = true;

        Circle rBall = new Circle(21, Color.RED);
        rBall.setTranslateX(4);
        rBall.setTranslateY(4);
        rBall.relocate(25 + 3*50,25 );
        mBoard.getChildren().add(rBall);
        mCircles[row][column] = rBall;

        EventHandler<javafx.event.ActionEvent> onT2Finished = eventTwo -> {
            addCandidateValueLabels(candidates);
            mGameCallback.computerMoveComplete();
        };

        if (column!= 3){
            KeyValue kv1 =  new KeyValue(rBall.centerXProperty(), (column-3)*50);
            EventHandler<javafx.event.ActionEvent> onT1Finished = event -> {
                KeyValue kv2 = new  KeyValue(rBall.layoutYProperty(), 46+ (NUM_ROWS- row)*50);
                KeyFrame f2 = new KeyFrame(Duration.millis(250),onT2Finished,kv2);
                Timeline t2 = new Timeline(f2);
                t2.play();

            };
            KeyFrame frame = new KeyFrame(Duration.millis(250), onT1Finished, kv1);
            Timeline t1 = new Timeline(frame);

            t1.play();
        }
        else{

            KeyValue kv2 = new  KeyValue(rBall.layoutYProperty(), 46+((NUM_ROWS- row)*50));
            KeyFrame f2 = new KeyFrame(Duration.millis(400),onT2Finished,kv2);
            Timeline t2 = new Timeline(f2);
            t2.play();
        }


    }

    public void setManualPlayerTurn(boolean manualPlayerTurn) {
        mManualPlayerTurn = manualPlayerTurn;
        if(mManualPlayerTurn){
            mSelectionBall = new Circle(21, Color.BLUE);
            mSelectionBall.setTranslateX(4);
            mSelectionBall.setTranslateY(4);
            mSelectionBall.relocate(25 ,25 );
            mBoard.getChildren().add(mSelectionBall);

        }
    }

    private int computeColumn(double position){
        if(position < 25){
            return 0;
        }

        if (position > 374){
            return 6;
        }

        int pos = (int)(position - 25);
        return pos/50;

    }

    public void showWinLine(List<Coordinate> positions) {
        for (Coordinate position : positions) {
            mCircles[position.getRow()][position.getColumn()].setStrokeType(StrokeType.INSIDE);
            mCircles[position.getRow()][position.getColumn()].setStrokeWidth(4.0);
            mCircles[position.getRow()][position.getColumn()].setStroke(Color.BLACK);
        }

    }
}
