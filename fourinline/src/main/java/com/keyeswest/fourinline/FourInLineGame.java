package com.keyeswest.fourinline;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameBoard;
import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.Scanner;

public class FourInLineGame extends Game {

    private FourInLineGame(FourInLineGame game){
        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mGameState = game.mGameState.makeCopy();
    }

    @Override
    public Game makeCopy() {
        return new FourInLineGame(this);
    }

    @Override
    public Move getOpponentMove() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter P2 move: ");
        System.out.flush();
        int selectedColumn = in.nextInt();
        in.nextLine();

        return new FourInLineMove(selectedColumn);
    }

    public FourInLineGame(GameBoard board, Player initialPlayer ){
        super(board, initialPlayer);
    }
}
