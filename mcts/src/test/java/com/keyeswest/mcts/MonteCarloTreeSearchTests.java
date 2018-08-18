package com.keyeswest.mcts;

import org.junit.Assert;
import org.junit.Test;

public class MonteCarloTreeSearchTests {

    @Test
    public void runSimulationTest(){
        MonteCarloTreeSearch mcts = new MonteCarloTreeSearch();
        mcts.runSimulation(null);
        Assert.assertTrue(true);
    }
}
