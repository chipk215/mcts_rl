package com.keyeswest.core;

import org.junit.Assert;
import org.junit.Test;

public class GameStatusTests {

    @Test
    public void makeCopyTest(){
        Player player = Player.P1;
        GameStatus gameStatus = new GameStatus();
        gameStatus.setWinningPlayer(null);

        GameStatus copyStatus = gameStatus.makeCopy();
        copyStatus.setWinningPlayer(player);
        copyStatus.setStatus(GameStatus.Status.GAME_WON);

        Assert.assertEquals(GameStatus.Status.IN_PROGRESS,gameStatus.getStatus());
        Assert.assertEquals(GameStatus.Status.GAME_WON, copyStatus.getStatus());

        Assert.assertNull(gameStatus.getWinningPlayer());
        Assert.assertEquals(player,copyStatus.getWinningPlayer());
    }
}
