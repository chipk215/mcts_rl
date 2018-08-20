package com.keyeswest.core;

import org.junit.Assert;
import org.junit.Test;

public class GameStatusTests {

    @Test
    public void makeCopyTest(){
        Player player = Player.P1;
        GameState gameStatus = new GameState(player);
        gameStatus.setWinningPlayer(null);

        GameState copyStatus = gameStatus.makeCopy();
        copyStatus.setWinningPlayer(player);
        copyStatus.setStatus(GameState.Status.GAME_WON);

        Assert.assertEquals(GameState.Status.IN_PROGRESS,gameStatus.getStatus());
        Assert.assertEquals(GameState.Status.GAME_WON, copyStatus.getStatus());

        Assert.assertNull(gameStatus.getWinningPlayer());
        Assert.assertEquals(player,copyStatus.getWinningPlayer());
    }
}
