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

import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Logger;

public class Board {

    private static final Logger LOGGER = Logger.getLogger(Board.class.getName());

    // Dimensions for scene
    private static final double PANE_WIDTH = 440;
    private static final double PANE_HEIGHT = 600;
    private static final int CELL_WIDTH_HEIGHT = 50;


    // Board dimensions
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 7;


    // User messages
    private final static String NEW_GAME_LABEL = "New Game";
    private final static String INITIALIZING_MESSAGE = "Initializing...";

    // The root of the display scene is a vbox
    private VBox mRoot;

    // Top node in vbox is a text node for messaging the user
    private Text mUserMessage;

    // Middle node is the game board
    private Pane mBoard;

    //Bottom node contains teh reset button for the game.
    private Button mResetButton;

    // Interface to call back to the game controller when required
    private GameCallback mGameCallback;

    // Holds image views corresponding to the ball containers
    private StackPane[][] mGrid;

    // Convenience holder containing the positioned player markers, used to quickly access the positioned
    // balls to mark the winning positions
    private Circle[][] mCircles;

    // Indicates whether a board position is empty (after mCircles was implemented this could be eliminated)
    private Boolean[][] mGridFill;

    // Data structure used to hold the values assigned to the corresponding move by the MCTS algorithm
    private Label[] mMoveValues;

    // Trie when it is not the computer's move
    private boolean mManualPlayerTurn;

    // The circle representing the user's move that animates along the top of the board before dropping into a column
    private Circle mSelectionBall;

    /**
     * Constructor invoked from game class
     */
    public Board(GameCallback gameCallback){
        mUserMessage = new Text();
        mGameCallback = gameCallback;
        mGrid = new StackPane[NUM_ROWS][NUM_COLS];
        mGridFill = new Boolean[NUM_ROWS][NUM_COLS];
        mMoveValues = new Label[NUM_COLS];
        mCircles = new Circle[NUM_ROWS][NUM_COLS];
    }


    /**
     * Sets up the game board display.
     * @return the root node of the display hierarchy.
     */
    public Parent createContent(){

        // root node configuration
        mRoot = new VBox();
        mRoot.setPrefSize(PANE_WIDTH, PANE_HEIGHT);
        mRoot.setPadding(new Insets(15));
        mRoot.setSpacing(10);

        // User text node configuration
        mUserMessage.setFont(Font.font(18));
        mUserMessage.setTextOrigin(VPos.TOP);
        mUserMessage.setText(INITIALIZING_MESSAGE);

        // The graphic for representing the ball container in a column
        // Note the dimensions fo the container image is 50x50 pixels
        Image cell = new Image("file:fourinline/images/container.png");

        // Middle node of the vbox
        mBoard = new Pane();
        mBoard.setPrefSize(400,400);
        mBoard.setPadding(new Insets(25));


        // Build and display the ball container columns
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                ImageView cellView = new ImageView(cell);
                mGrid[row][col] = new StackPane();
                mGrid[row][col].getChildren().add(cellView);
                mGrid[row][col].relocate(25 + (col * CELL_WIDTH_HEIGHT), 75 + (row * CELL_WIDTH_HEIGHT));
                mBoard.getChildren().add(mGrid[row][col]);
                mGridFill[row][col] = false;
            }
        }

        // Create the labels for displaying the MCTS move values below the columns
        for (int col=0; col< NUM_COLS; col++){
            Label label = new Label("");
            label.setTranslateX(35 + col*CELL_WIDTH_HEIGHT);
            label.setFont(Font.font(10));
            label.setTranslateY(30+NUM_COLS*CELL_WIDTH_HEIGHT );
            mBoard.getChildren().add(label);
            mMoveValues[col] = label;
        }


        // Set up the moise move event handler used to allow the user to select a column to drop the ball
        mBoard.setOnMouseMoved(event -> {
            int lastBallX=0;

            // Only need to track the mouse when it is not the computer's move
            if (mManualPlayerTurn){

                // User clicks teh mouse to indicate column selection
                mBoard.setOnMouseClicked(clickEvent ->{

                    // Compute the column number from the X position of the animated user circle
                    int ballColumn = computeColumn(mSelectionBall.getLayoutX());

                    //Ignore the click if the column is full
                    if ( ! mGridFill[0][ballColumn]){

                        // Drop the ball into the selected column by finding the first empty row from the bottom
                        for(int row=NUM_ROWS-1; row>=0; row--){
                            if(! mGridFill[row][ballColumn]){
                                mGridFill[row][ballColumn] = true;

                                mSelectionBall.relocate(25 + ballColumn*CELL_WIDTH_HEIGHT,
                                        25+ (row+1)*CELL_WIDTH_HEIGHT);
                                mCircles[NUM_ROWS-row-1][ballColumn] = mSelectionBall;
                                break;
                            }
                        }
                        // manual move complete
                        mManualPlayerTurn = false;
                        // send move back to game
                        mGameCallback.opponentMove(new FourInLineMove(ballColumn));
                    }
                });

                // Move ball across the columns tracking the user's mouse movement
                double mouseX =event.getX();
                double ballX = mSelectionBall.getLayoutX();
                int ballColumn = computeColumn(ballX) +1;
                int mouseColumn = computeColumn(mouseX)+1;
                if (mouseColumn != ballColumn) {
                    lastBallX = 25+(mouseColumn-1)*CELL_WIDTH_HEIGHT;
                    mSelectionBall.relocate(lastBallX,25);
                }
            }
        });

        // Configure the reset button
        mResetButton = new Button(NEW_GAME_LABEL);
        mResetButton.setOnAction(event -> mGameCallback.resetGame());
        mRoot.getChildren().addAll(mUserMessage, mBoard, mResetButton);
        mRoot.setAlignment(Pos.TOP_CENTER);

        return mRoot;
    }


    /**
     * Display message to user
     * @param message - message to display
     */
    public void setUserMessage(String message) {
        mUserMessage.setText(message);
    }


    /**
     * Helper method for displaying MCTS move values
     * @param  - the MCTS values for each considered move
     */
    private void addCandidateValueLabels(List<MoveValue> candidates){
        for (int col =0; col < NUM_COLS; col++){
            mMoveValues[col].setText("");
        }
        if (candidates != null){
            for (MoveValue mv : candidates){
                FourInLineMove move = (FourInLineMove)mv.getMove();
                double moveValue = mv.getValue();
                DecimalFormat df = new DecimalFormat("#.####");
                mMoveValues[move.getColumn()].setText(df.format(moveValue));
            }
        }
    }

    /**
     * Display the computer move
     * @param row - the row coresponding to the position within a column
     * @param column - the column corresponding to the move
     * @param candidates - the MCTS values for all considered moves
     */
    public void showComputerMove(int row, int column, List<MoveValue> candidates){
        // Mark the destination cell as filled
        mGridFill[NUM_ROWS-row-1][column] = true;

        // Create the computer's move marker (red ball)
        Circle rBall = new Circle(21, Color.RED);
        rBall.setTranslateX(4);
        rBall.setTranslateY(4);
        // Position the ball above the middle column
        rBall.relocate(25 + 3*CELL_WIDTH_HEIGHT,25 );
        mBoard.getChildren().add(rBall);
        // Save the ball in the ball store
        mCircles[row][column] = rBall;

        // The animation associated with placing the computer ball moves the ball along the top of the board
        // beginning at the middle column and ending at the column corresponding to the move's position.
        // If the desired move is the middle column, not horizontal movement s made.
        //
        // The ball is then dropped into the desired column.

        // Since the animation is asynchronous we don't want to have the game initiate the next move until
        // the animation is complete, so a callback method is invoked when the animation completes.
        EventHandler<javafx.event.ActionEvent> onT2Finished = eventTwo -> {
            addCandidateValueLabels(candidates);
            mGameCallback.computerMoveComplete();
        };

        // If move is not to middle column
        if (column!= (NUM_COLS/2)){
            // Animate moving the ball horizontally to the desired column
            KeyValue kv1 =  new KeyValue(rBall.centerXProperty(), (column-3)*CELL_WIDTH_HEIGHT);

            // On completing the horizontal animation, begin a 2nd transition to drop the ball into the column
            EventHandler<javafx.event.ActionEvent> onT1Finished = event -> {
                KeyValue kv2 = new  KeyValue(rBall.layoutYProperty(), 46+ (NUM_ROWS- row)*CELL_WIDTH_HEIGHT);
                KeyFrame f2 = new KeyFrame(Duration.millis(250),onT2Finished,kv2);
                Timeline t2 = new Timeline(f2);
                t2.play();

            };
            KeyFrame frame = new KeyFrame(Duration.millis(250), onT1Finished, kv1);
            Timeline t1 = new Timeline(frame);

            t1.play();
        }
        else{
            // The desired column is the middle column so just drop the ball
            KeyValue kv2 = new  KeyValue(rBall.layoutYProperty(), 46+((NUM_ROWS- row)*CELL_WIDTH_HEIGHT));
            KeyFrame f2 = new KeyFrame(Duration.millis(400),onT2Finished,kv2);
            Timeline t2 = new Timeline(f2);
            t2.play();
        }

    }

    /**
     * Initiate the manual player move.
     * Create the ball corresponding to the player's move
     * @param manualPlayerTurn: true when it is the manual player's turn
     */
    public void setManualPlayerTurn(boolean manualPlayerTurn) {
        mManualPlayerTurn = manualPlayerTurn;
        if(mManualPlayerTurn){
            mSelectionBall = new Circle(21, Color.BLUE);
            mSelectionBall.setTranslateX(4);
            mSelectionBall.setTranslateY(4);
            // position the ball on top of the board so the user can move it to the desired column
            mSelectionBall.relocate(25 ,25 );
            mBoard.getChildren().add(mSelectionBall);

        }
    }


    /**
     * Compute the column number corresponding to an X value in the game board coordinate space
     * @param position X value in the game board coordinate space
     * @return column number 0 through 6
     */
    private int computeColumn(double position){
        if(position < 25){
            return 0;
        }

        if (position > 24 + (NUM_COLS*CELL_WIDTH_HEIGHT)){
            return NUM_COLS-1;
        }

        int pos = (int)(position - 25);
        return pos/CELL_WIDTH_HEIGHT;

    }

    /**
     * Mark the winning positions by applying a black outline to the ball
     * @param positions
     */
    public void showWinLine(List<Coordinate> positions) {
        for (Coordinate position : positions) {
            mCircles[position.getRow()][position.getColumn()].setStrokeType(StrokeType.INSIDE);
            mCircles[position.getRow()][position.getColumn()].setStrokeWidth(4.0);
            mCircles[position.getRow()][position.getColumn()].setStroke(Color.BLACK);
        }

    }
}
