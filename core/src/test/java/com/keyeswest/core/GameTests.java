package com.keyeswest.core;

import org.junit.Assert;
import org.junit.Test;

public class GameTests {

    @Test
    public void makeCopyTests(){
        Player player = Player.P1;
        GameTestBoard board = new GameTestBoard();
        Game game = new Game(board, player);

        Game copyGame = game.makeCopy();
        copyGame.setStatus(GameStatus.Status.GAME_WON);
        copyGame.setWinner(Player.P1.getOpponent());

        Assert.assertEquals(GameStatus.Status.IN_PROGRESS, game.getStatus());
        Assert.assertEquals(GameStatus.Status.GAME_WON, copyGame.getStatus());

        Assert.assertNull(game.getWinner());
        Assert.assertEquals(Player.P1.getOpponent(), copyGame.getWinner());

    }
}
