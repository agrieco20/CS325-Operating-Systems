/* File: CentralRandom.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A singleton wrapper for an random number generator so all randomness can be
 *  properly seeded and thus reproduced.
 */


package com.sos.generator;

import java.util.Random;

public class CentralRandom {
    private static Random rng = null;

    public static Random getRNG(){
        if(rng == null)rng = new Random();
        return rng;
    }

    public static Random getRNG(long seed){
        if(rng == null)rng = new Random(seed);
        rng.setSeed(seed);
        return rng;
    }

    public static void destroy(){
        rng = null;
    }
}
