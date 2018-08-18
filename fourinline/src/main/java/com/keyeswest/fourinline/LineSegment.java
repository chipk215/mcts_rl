package com.keyeswest.fourinline;

class LineSegment {


    private int mStartRow;
    private int mStartColumn;

    private int mEndRow;
    private int mEndColumn;

    LineSegment(int startRow, int startColumn, int endRow, int endColumn){
        mStartRow = startRow;
        mStartColumn = startColumn;
        mEndRow = endRow;
        mEndColumn = endColumn;
    }

    public int getStartRow() {
        return mStartRow;
    }

    public int getStartColumn() {
        return mStartColumn;
    }

    public int getEndRow() {
        return mEndRow;
    }

    public int getEndColumn() {
        return mEndColumn;
    }

    public int getLength(){
        // since the slope is always one, choose either dimension and return the difference from end to start plus one
        return mEndColumn - mStartColumn + 1;
    }

}
