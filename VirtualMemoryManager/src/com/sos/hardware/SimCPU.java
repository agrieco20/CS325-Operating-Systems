/* File: SimCPU.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.hardware;

import com.sos.bookkeeping.Logger;
import com.sos.os.SimProcess;

public class SimCPU {
    //Instance variables
    private int cycleCount;

    public SimCPU(){
        cycleCount = 0;
    }

    public int run_burst(SimProcess process, int amount){
        int cycles = process.run_cycles(amount);
        cycleCount += cycles;
        return cycles;
    }

    public void run_idle(int amount){
        cycleCount += amount;
    }

    public int getCycleCount(){
        return cycleCount;
    }
}
