package com.keyeswest.tictactoe;

import com.keyeswest.core.Game;
import com.keyeswest.core.GameBoard;

import com.keyeswest.core.Move;
import com.keyeswest.core.Player;

import java.util.Scanner;

public class TicTacToeGame extends Game {

    private TicTacToeGame(TicTacToeGame game){
        mGameBoard = game.mGameBoard.getCopyOfBoard();
        mGameState = game.mGameState.makeCopy();
    }

    @Override
    public Game makeCopy() {
        return new TicTacToeGame(this);
    }

    @Override
    public Move getOpponentMove() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter P2 row move: ");
        int selectedRow = in.nextInt();
        in.nextLine();
        System.out.println("Enter P2 column move: ");
        System.out.flush();
        int selectedColumn = in.nextInt();
        in.nextLine();

        return new TicTacToeMove(selectedRow, selectedColumn);
    }

    public TicTacToeGame(GameBoard board, Player initialPlayer ){
        super(board, initialPlayer);
    }
}
