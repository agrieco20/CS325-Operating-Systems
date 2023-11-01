/* File: SimProcessInfo.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.generator.CentralRandom;

public class SimProcessInfo {
    private final SimProcess process;
    private int estCompTime;
    private final double error;

    public SimProcessInfo(SimProcess process){
        this.process = process;
        this.error = 1.0 + ((0.2 * CentralRandom.getRNG().nextDouble()) - 0.4);
        calculateCompletionTime();
    }

    public int getEstCompTime(){
        return estCompTime;
    }

    public int getPid(){
        return process.getPid();
    }

    public int getPriority(){
        return process.getPriority();
    }

    public SimProcessState getState(){
        return process.getState();
    }
    public SimPage getPage(int addr){
        return process.getPage(addr);
    }

    public void setState(SimProcessState state){
        process.setState(state);
    }

    public int calculateCompletionTime(){
        estCompTime = (int)(error * process.completionTime());
        return estCompTime;
    }
}
