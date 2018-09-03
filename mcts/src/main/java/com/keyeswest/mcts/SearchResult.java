package com.keyeswest.mcts;

import com.keyeswest.core.MoveValue;
import com.keyeswest.core.Move;

import java.util.List;

public class SearchResult {

    private List<MoveValue>  mCandidates;
    private Move mSelected;

    public SearchResult(List<MoveValue> candidates, Move selected){
        mCandidates = candidates;
        mSelected = selected;
    }

    public List<MoveValue> getCandidates() {
        return mCandidates;
    }

    public Move getSelected() {
        return mSelected;
    }
}
