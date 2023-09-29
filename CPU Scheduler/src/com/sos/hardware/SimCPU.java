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
    private final int burstAmount;

    public SimCPU(){
        cycleCount = 0;
        burstAmount = 20;
    }

    public SimCPU(int burstAmount){
        cycleCount = 0;
        this.burstAmount = burstAmount;
    }

    public void run_burst(SimProcess process, int pid){
        int cycles = process.run_cycles(burstAmount);
        Logger.getLog().log(String.format("Ran %d cycles on process %d.", cycles, pid));
        cycleCount += cycles;
    }

    public int getCycleCount(){
        return cycleCount;
    }
}
