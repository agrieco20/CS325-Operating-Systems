/* File: SimProcess.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 * Notes:
 *  Add in memory access system.
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;

public class SimProcess {
    private final SimProgram baseProgram;
    private int cycleCount;
    private int programInstruction;
    private int remainingCyclesOnInstr;
    private int createdCycle;
    private SimProcessState state;

    public SimProcess(SimProgram base_program, int createdCycle){
        this.baseProgram = base_program;
        cycleCount = 0;
        programInstruction = 0;
        remainingCyclesOnInstr = this.baseProgram.getInstr(programInstruction).getCycleCount();
        state = SimProcessState.READY;
        this.createdCycle = createdCycle;
    }

    public int run_cycles(int cycles){
        if(state == SimProcessState.WAITING || state == SimProcessState.TERMINATED){
            Logger.getLog().error("Attempted to run a WAITING or COMPLETE process.");
            return 0;
        }
        state = SimProcessState.RUNNING;
        for(int i = 0;i < cycles;i++) {
            remainingCyclesOnInstr -= 1;
            cycleCount += 1;
            //Move to next instructions
            if (remainingCyclesOnInstr <= 0) {
                programInstruction = baseProgram.getInstr(programInstruction).getNextInstructionIndex();
                if(!baseProgram.validInstr(programInstruction))
                    state = SimProcessState.TERMINATED;
                else
                    remainingCyclesOnInstr = baseProgram.getInstr(programInstruction).getCycleCount();
                return (i + 1);
            }
        }
        return cycles;
    }

    public SimInstruction getCurrentInstruction(){
        if(!baseProgram.validInstr(programInstruction))return null;
        return baseProgram.getInstr(programInstruction);
    }

    public void setState(SimProcessState new_state){
        state = new_state;
    }

    public int completionTime(){
        return (baseProgram.completionTime() - cycleCount);
    }

    public SimProcessState getState(){
        return state;
    }

    public int getWaitCycles(int cpuCycle){
        return (cpuCycle - createdCycle) - cycleCount;
    }
}
