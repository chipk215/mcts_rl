# Monte Carlo Tree Search Games

This work is intended to provide an exploratory framework for using Monte Carlo Tree Search for various single player and two player games.

The framework is under development and most likely has some defects that have arisen as a second game has been added which provided more insights into abstracting a specific game from the Monte Carlo search algorithm.  When the framework is more stable I'll update this note.

## QuickStart
The GameControllerApp class is used to specify which game to run, either Tic-Tac-Toe or FourInLine, by commenting and uncommenting code in the start method:

```
@Override
    public void start(Stage primaryStage) {
        setPrimaryStage(primaryStage);
        startNewFourInLineGame(P2);
        //startNewTicTacGame(P1);
    }
```

The program is then executed by running GameControllerApp (I'm running inside IntelliJ during development).

More to come...


  

