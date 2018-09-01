package com.keyeswest.fourinline.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Board {

    private static double PANE_WIDTH = 440;
    private static double PANE_HEIGHT = 600;

    private final static String NEW_GAME_LABEL = "New Game";
    private final static String INITIALIZING_MESSAGE = "Initializing...";

    private VBox mRoot;
    private Text mUserMessage;
    private Button mResetButton;

    private double ballPos;

    public Board(){
        mUserMessage = new Text();
    }


    public Parent createContent(){
        mRoot = new VBox();
        mRoot.setPrefSize(PANE_WIDTH, PANE_HEIGHT);

        mRoot.setPadding(new Insets(15));
        mRoot.setSpacing(10);

        mUserMessage.setFont(Font.font(18));
        mUserMessage.setTextOrigin(VPos.TOP);
        mUserMessage.setText(INITIALIZING_MESSAGE);

        Canvas canvas = new Canvas(400, 400);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        Image cell = new Image("file:fourinline/images/container.png");
        Image redBall = new Image("file:fourinline/images/redBall.png");
        Image blank = new Image("file:fourinline/images/white.png");

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                gc.drawImage(cell, 25 + (col * 50), 75 + (row * 50));
            }
        }
        gc.drawImage(redBall, 25, 25);

        ballPos = 25;
        canvas.setOnMouseMoved(event -> {

            double mouseX =event.getX();
            int ballColumn = computeColumn(ballPos);
            int mouseColumn = computeColumn(mouseX);
            if (mouseColumn != ballColumn){
                gc.drawImage(blank, ballPos, 25);
                ballPos = 25 + (mouseColumn-1)*50;

                gc.drawImage(redBall, ballPos, 25);
            }

        });


        mResetButton = new Button(NEW_GAME_LABEL);
        //mResetButton.setOnAction(event -> mGameCallback.resetGame());
        mRoot.getChildren().addAll(mUserMessage, canvas, mResetButton);
        mRoot.setAlignment(Pos.TOP_CENTER);

        return mRoot;
    }

    private int computeColumn(double position){
        if(position < 25){
            return 1;
        }

        if (position > 374){
            return 7;
        }

        int pos = (int)(position - 25);
        return pos/50 +1;

    }
}
