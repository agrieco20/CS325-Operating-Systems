/* File: SimProcessControl.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */

//TODO: Add in error checking (memory, resource, etc)

package com.sos.os;

import com.sos.bookkeeping.Logger;
import com.sos.bookkeeping.Statistics;
import com.sos.generator.CentralRandom;
import com.sos.hardware.SimCPU;
import com.sos.hardware.SimRAM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimOS {
    //Constants
    private static final int GC_FREQ = 20;
    public static final int RESOURCES = 5;
    //Class variables
    private static int nextPid = 0;

    //Instance variables
    private final HashMap<Integer, SimProcess> processMap;
    private final ProcessScheduler scheduler;
    private final MemoryManager memoryManager;
    private final AccessManager accessManager;
    private final SimCPU cpu;
    private final SimRAM ram;
    private int stepCounter;
    private double avgWait;
    private int processCount;
    private final HashMap<Integer, SimResource> resourceMap;

    public SimOS(ProcessScheduler scheduler, MemoryManager memoryManager, AccessManager accessManager){
        processMap = new HashMap<>();
        resourceMap = new HashMap<>();
        this.scheduler = scheduler;
        this.memoryManager = memoryManager;
        this.accessManager = accessManager;
        for(int i = 0;i < RESOURCES;i++){
            SimResource temp = new SimResource();
            this.resourceMap.put(temp.getID(), temp);
            this.accessManager.addResource(i);
        }
        cpu = new SimCPU();
        ram = new SimRAM();
        stepCounter = 0;
        avgWait = 0;
    }

    public void add_process(SimProcess process){
        int pid = nextPid++;
        int priority = CentralRandom.getRNG().nextInt(7);
        processMap.put(pid, process);
        Logger.getLog().log(String.format("New process added. Pid: %d and Priority: %d.", pid, priority));
        String stats_key = String.format("Add P%d:", pid);
        Statistics.getStatLog().register(stats_key, cpu.getCycleCount());
        scheduler.addProcess(new SimProcessInfo(process,pid, priority));
    }

    public void run_step(){
        int process = scheduler.getNextProcess();
        if(process == -1){
            collect_garbage();
            Logger.getLog().log("No active process. Idling.");
            return;
        }
        SimProcess current = processMap.get(process);
        if(current.getState() == SimProcessState.WAITING){
            Logger.getLog().error(String.format("Burst attempted on process %d, which is waiting.", process));
            return;
        }
        if(current.getState() == SimProcessState.TERMINATED){
            Logger.getLog().error(String.format("Burst attempted on process %d, which is terminated.", process));
            return;
        }
        if(current.getState() == SimProcessState.READY)current.setState(SimProcessState.RUNNING);
        Logger.getLog().log(String.format("Running burst on process %d.", process));
        SimInstruction instruction = current.getCurrentInstruction();
        memoryManager.requestMemory(process, instruction.getInstructionAddress(), ram);
        if(instruction.isMemoryInstruction())
            memoryManager.requestMemory(process, instruction.getMemoryAccess(), ram);
        else if(instruction.isResourceInstruction()) {
            int resourceID = Math.abs(instruction.getResourceAccess());
            SimResource resource = resourceMap.get(resourceID);
            if (instruction.getResourceAccess() < 0) {
                accessManager.releaseResource(process, resourceID);
                resource.releaseControl(process);
            }
            else {
                boolean result = accessManager.requestResource(process, resourceID);
                if (result) {
                    resource.addController(process);
                } else if (!resource.hasControl(process)) {
                    current.setState(SimProcessState.WAITING);
                    Logger.getLog().log(String.format("Process %d held after requesting resource %d.",
                            process, resourceID));
                }
            }
        }
        if(current.getState() == SimProcessState.WAITING) return;
        cpu.run_burst(processMap.get(process), process);
        stepCounter += 1;
        if(stepCounter%GC_FREQ == 0) {
            stepCounter = 0;
            collect_garbage();
        }
    }

    public boolean idle(){
        return (processMap.size() == 0);
    }

    public void collect_garbage(){
        ArrayList<Integer> removals = new ArrayList<>();
        for(Integer key : processMap.keySet()){
            if(processMap.get(key).getState() == SimProcessState.TERMINATED){
                Logger.getLog().log(String.format("Process %d complete and removed from system.", key));
                String stats_key = String.format("Complete P%d:", key);
                Statistics.getStatLog().register(stats_key, cpu.getCycleCount());
                stats_key = String.format("Wait P%d:", key);
                Statistics.getStatLog().register(stats_key, processMap.get(key).getWaitCycles(cpu.getCycleCount()));
                avgWait += processMap.get(key).getWaitCycles(cpu.getCycleCount());
                processCount += 1;
                removals.add(key);
            }
        }
        for(Integer key : removals){
            processMap.remove(key);
        }
    }

    public int currentProcesses(){
        return processMap.size();
    }

    public int getCPUCycleCount(){
        return cpu.getCycleCount();
    }

    public double getAvgWait(){
        return avgWait/processCount;
    }
}
