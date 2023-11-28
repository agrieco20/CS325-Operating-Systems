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
import com.sos.hardware.SimRAM;

public class SimProcess {
    private final SimProgram baseProgram;
    private final int pid;
    private int cycleCount;
    private int programInstruction;
    private int remainingCyclesOnInstr;
    private final int createdCycle;
    private int priority;
    private SimProcessState state;
    private boolean partialInstr;
    private final SimPage[] pages;

    public SimProcess(SimProgram baseProgram, int pid, int createdCycle){
        this.baseProgram = baseProgram;
        this.pid = pid;
        cycleCount = 0;
        programInstruction = 0;
        remainingCyclesOnInstr = this.baseProgram.getInstr(programInstruction).getCycleCount();
        state = SimProcessState.READY;
        this.createdCycle = createdCycle;
        partialInstr = false;
        int numPages = (this.baseProgram.total_size()/OSConstants.pageSize)+1;
        pages = new SimPage[numPages];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new SimPage(this.pid, i);
        }
        priority = -1;
    }

    public int run_cycles(int cycles){
        if(state == SimProcessState.WAITING || state == SimProcessState.TERMINATED){
            Logger.error_cpu("Attempted to run a WAITING or COMPLETE process.");
            return 0;
        }
        state = SimProcessState.RUNNING;
        for(int i = 0;i < cycles;i++) {
            remainingCyclesOnInstr -= 1;
            cycleCount += 1;
            //Move to next instructions
            if (remainingCyclesOnInstr <= 0) {
                partialInstr = false;
                programInstruction = baseProgram.getInstr(programInstruction).getNextInstructionIndex();
                if(!baseProgram.validInstr(programInstruction)) {
                    state = SimProcessState.TERMINATED;
                }
                else
                    remainingCyclesOnInstr = baseProgram.getInstr(programInstruction).getCycleCount();
                return (i + 1);
            }
            else{
                partialInstr = true;
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

    public boolean isPartialInstr() {
        return partialInstr;
    }

    public void setPriority(int val){
        if(priority == -1)priority = val;
    }

    public int getPriority(){
        return priority;
    }

    public int getPid(){
        return pid;
    }

    public SimPage getPage(int address){
        if(address < 0 || address >= baseProgram.total_size()){
            Logger.error_mem(String.format("Address %d is outside of the address space of process %d",
                    address, pid));
            return null;
        }
        return pages[address/OSConstants.pageSize];
    }

    public int getPageNum(int address){
        if(address < 0 || address >= baseProgram.total_size()){
            Logger.error_mem(String.format("Address %d is outside of the address space of process %d",
                    address, pid));
            return -1;
        }
        return address/OSConstants.pageSize;
    }

    public int pageCount(){
        return pages.length;
    }
}
