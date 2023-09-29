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
    private final int pid;
    private final int priority;

    public SimProcessInfo(SimProcess process, int pid, int priority){
        this.process = process;
        this.pid = pid;
        this.priority = priority;
        calculateCompletionTime();
    }

    public int getEstCompTime(){
        return estCompTime;
    }

    public int getPid(){
        return pid;
    }

    public int getPriority(){
        return priority;
    }

    public SimProcessState getState(){
        return process.getState();
    }

    public void set_state(SimProcessState state){
        process.setState(state);
    }

    public int calculateCompletionTime(){
        double error = 1.0 + ((0.2 * CentralRandom.getRNG().nextDouble()) - 0.4);
        estCompTime = (int)(error * process.completionTime());
        return estCompTime;
    }
}
