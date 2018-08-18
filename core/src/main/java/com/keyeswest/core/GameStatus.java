package com.keyeswest.core;

public class GameStatus {
    private Status mStatus;

    private Player mWinningPlayer;

    private GameStatus(GameStatus status){
        mStatus = status.mStatus;
        mWinningPlayer = status.mWinningPlayer;
    }

    public GameStatus(){
        mStatus = Status.IN_PROGRESS;
    }

    public void setWinningPlayer(Player winningPlayer) {
        mWinningPlayer = winningPlayer;
    }


    public enum Status{
        IN_PROGRESS, GAME_WON, GAME_TIED
    }

    public Status getStatus() {
        return mStatus;
    }

    public Player getWinningPlayer() {
        return mWinningPlayer;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public GameStatus makeCopy(){
        return new GameStatus(this);
    }
}
