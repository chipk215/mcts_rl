package com.keyeswest.core;

public  class RandomIntegerGenerator {

    // returns integer x in range 0<= x < maximum
    public static int randomIntegerIndex(int maximum){
        return (int)(Math.random() * (maximum));
    }
}
