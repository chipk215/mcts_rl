package com.keyeswest.core;


import org.junit.Assert;
import org.junit.Test;

import java.util.Hashtable;

public class GameStatusTests {

    private static int MAXIMUM = 5;


    @Test
    public void randomTest(){
        Hashtable<Integer, Integer> distribution;
        distribution = new Hashtable<>();

        for (int i=0; i<=MAXIMUM; i++){
            distribution.put(i,0);
        }

        for (int i=0; i< 1000; i++){
            int randomInt = RandomIntegerGenerator.randomIntegerIndex(MAXIMUM);
            distribution.put(randomInt, distribution.get(randomInt)+1);
        }

        for (int i=0; i<=MAXIMUM; i++){
            System.out.println("Key: " + Integer.toString(i) + "  Count= " + Integer.toString(distribution.get(i)));
        }

        Assert.assertEquals(0, (int) distribution.get(MAXIMUM));
    }
}
