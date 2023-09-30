/* File: SimUser.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A simulated user who runs processes on their computer.
 *  Represented by a 2-state Markov Model.
 */


package com.sos.generator;

import com.sos.os.SimOS;
import com.sos.os.SimProcess;
import com.sos.os.SimProgram;

import java.util.ArrayList;

public class SimUser {
    //Constants
    public static int IDLE_TO_IDLE = 95;
    public static int PROCESS_TO_PROCESS = 60;
    //Instance Variables
    private int processes;
    private String currentState;
    private final ArrayList<SimProgram> programs;

    public SimUser(int programs, int processes){
        this.programs = new ArrayList<>();
        for(int i = 0;i < programs;i++){
            this.programs.add(new SimProgram());
        }
        this.processes = processes;
        currentState = "PROCESS";
    }

    public void nextStep(SimOS os){
        if(processes == 0)return;
        int roll = CentralRandom.getRNG().nextInt(100);
        if(currentState.equals("IDLE")){
            if(roll > IDLE_TO_IDLE)currentState = "PROCESS";
        }
        else{
            if(roll > PROCESS_TO_PROCESS)currentState = "IDLE";
            int programIdx = CentralRandom.getRNG().nextInt(programs.size());
            os.add_process(new SimProcess(programs.get(programIdx), os.getCPUCycleCount()));
            processes -= 1;
        }
    }

    public boolean finished(){
        return processes == 0;
    }

    public int remainingProcesses() {
        return processes;
    }
}
