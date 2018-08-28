package com.keyeswest.tictactoe.view;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoardCell extends StackPane {
    private int mRow;
    private int mColumn;

    private Text mMarker;

    private boolean mMarked;

    private CellClickHandler mCellClickHandler;

    public BoardCell(int row, int column, CellClickHandler clickHandler){
        mRow = row;
        mColumn = column;
        mCellClickHandler = clickHandler;
        mMarker = new Text();
        mMarked = false;

        Rectangle border = new Rectangle(75,75);
        border.setFill(null);
        border.setStroke(Color.BLACK);
        setAlignment(Pos.CENTER);

        mMarker.setFont(Font.font(72));
        mMarker.setText(" ");

        getChildren().addAll(border,mMarker);

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

}
