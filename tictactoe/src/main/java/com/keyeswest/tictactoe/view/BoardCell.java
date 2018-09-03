package com.keyeswest.tictactoe.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoardCell extends StackPane {
    private int mRow;
    private int mColumn;

    private Text mMarker;

    private VBox mVbox;

    private Label mLabel;

    private boolean mMarked;

    private CellClickHandler mCellClickHandler;

    public BoardCell(int row, int column, CellClickHandler clickHandler){

        mVbox = new VBox();
        mVbox.setSpacing(2);
        mVbox.setAlignment(Pos.CENTER);

        mRow = row;
        mColumn = column;
        mCellClickHandler = clickHandler;
        mMarker = new Text();
        mMarked = false;

        Rectangle border = new Rectangle(75,75);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        setAlignment(Pos.CENTER);

        mMarker.setFont(Font.font(48));
        mMarker.setText(" ");

        mLabel = new Label("");
        mLabel.setFont(Font.font(10));

        mVbox.getChildren().addAll(mMarker,mLabel);

        getChildren().addAll(border,mVbox);

        setOnMouseClicked(event -> {
            mCellClickHandler.onCellClicked(this);
        });


    }

    public void drawX(){
        mMarker.setText("X");
        mMarked = true;
    }

    public void drawO(){
        mMarker.setText("O");
        mMarked = true;
    }

    public boolean isMarked(){
        return mMarked;
    }

    public int getRow(){
        return mRow;
    }

    public int getColumn(){
        return mColumn;
    }

    public void highlightBackground(){
        setStyle("-fx-background-color: lightgrey;");
    }

    public void setValue(String value){
        mLabel.setText(value);
    }

}
