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
import com.sos.hardware.SimHDD;
import com.sos.hardware.SimRAM;
import com.sos.os.instructions.MemoryInstruction;
import com.sos.os.instructions.ResourceInstruction;

import java.util.ArrayList;
import java.util.HashMap;

public class SimOS {
    //Constants
    private static final int GC_FREQ = 20;
    public static final int RESOURCES = 10;
    public static final int BURST_CYCLES = 40;
    //Class variables
    private static int nextPid = 0;

    //Instance variables
    private final HashMap<Integer, SimProcess> processMap;
    private final ProcessScheduler scheduler;
    private final MemoryManager memoryManager;
    private final AccessManager accessManager;
    private final SimCPU cpu;
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
            this.accessManager.addResource(temp.getID());
        }
        cpu = new SimCPU();
        stepCounter = 0;
        avgWait = 0;
    }

    public void add_process(SimProgram program){
        int pid = nextPid++;
        SimProcess process = new SimProcess(program, pid, cpu.getCycleCount());
        int priority = CentralRandom.getRNG().nextInt(7);
        process.setPriority(priority);
        processMap.put(pid, process);
        Logger.log_cpu(String.format("New process added. Pid: %d and Priority: %d.", pid, priority));
        String stats_key = String.format("Add P%d", pid);
        Statistics.getStatLog().register(stats_key, cpu.getCycleCount());
        scheduler.addProcess(new SimProcessInfo(process));
    }

    public void run_step(){
        int process = scheduler.getNextProcess();
        if(process == -1){
            collect_garbage();
            Logger.log_cpu("No active process. Idling.");
            return;
        }
        SimProcess current = processMap.get(process);
        if(current.getState() == SimProcessState.WAITING){
            Logger.error_cpu(String.format("Burst attempted on process %d, which is waiting.", process));
            return;
        }
        if(current.getState() == SimProcessState.TERMINATED){
            Logger.error_cpu(String.format("Burst attempted on process %d, which is terminated.", process));
            return;
        }
        if(current.getState() == SimProcessState.READY)current.setState(SimProcessState.RUNNING);
        Logger.log_cpu(String.format("Running burst on process %d.", process));
        int cycles = 0;
        while(cycles < BURST_CYCLES) {
            if (current.getState() == SimProcessState.WAITING || current.getState() == SimProcessState.TERMINATED) break;
            SimInstruction instruction = current.getCurrentInstruction();
            if(!current.isPartialInstr()){
                if(!processInstruction(instruction, new SimProcessInfo(processMap.get(process)))) {
                    int busyWaiting = BURST_CYCLES - cycles;
                    cpu.run_idle(busyWaiting);
                    break;
                }
            }
            if (current.getState() == SimProcessState.WAITING || current.getState() == SimProcessState.TERMINATED) break;
            cycles += cpu.run_burst(processMap.get(process), BURST_CYCLES - cycles);
        }
        stepCounter += 1;
        if (stepCounter % GC_FREQ == 0) {
            stepCounter = 0;
            collect_garbage();
        }
        Logger.log_cpu(String.format("Ran %d cycles on process %d.", cycles, process));
    }

    public boolean processInstruction(SimInstruction instr, SimProcessInfo info){
        if(instr == null)return false;
        if(!SimRAM.getInstance().contains(info.getPid(), instr.getInstructionAddress()))
            Logger.log_mem(String.format("Page fault for process %d.", info.getPid()));
        memoryManager.readRequest(info, instr.getInstructionAddress());
        performRead(info, instr.getInstructionAddress());
        if(instr instanceof MemoryInstruction memInstr){
            if(!SimRAM.getInstance().contains(info.getPid(), memInstr.getMemoryAddress()))
                Logger.log_mem(String.format("Page fault for process %d.", info.getPid()));
            if(memInstr.isWrite()) {
                memoryManager.writeRequest(info, memInstr.getMemoryAddress());
                performWrite(info, memInstr.getMemoryAddress());
            }
            else{
                memoryManager.readRequest(info, memInstr.getMemoryAddress());
                performRead(info, memInstr.getMemoryAddress());
            }
        }
        else if(instr instanceof ResourceInstruction resInstr){
            int resID = resInstr.getResource();
            SimResource resource = resourceMap.get(resID);
            if(resInstr.isRequest()){
                boolean result = accessManager.requestResource(info, resID, resInstr.isWrite());
                if (result) {
                    resource.addController(info.getPid(), resInstr.isWrite());
                } else if (!resource.hasControl(info.getPid())) {
                    Logger.log_res(String.format("Process %d did not get control of resource %d.", info.getPid(), resID));
                    return false;
                }
            }
            else{
                accessManager.releaseResource(info, resID);
                resource.releaseControl(info.getPid());
            }
        }
        return true;
    }

    public boolean idle(){
        return (processMap.size() == 0);
    }

    public void collect_garbage(){
        ArrayList<Integer> removals = new ArrayList<>();
        for(Integer key : processMap.keySet()){
            if(processMap.get(key).getState() == SimProcessState.TERMINATED){
                SimRAM.getInstance().freeAll(key);
                SimHDD.getInstance().freeAll(key);
                for(SimResource res : resourceMap.values()){
                    if(res.hasControl(key)) {
                        SimProcessInfo info = new SimProcessInfo(processMap.get(key));
                        accessManager.releaseResource(info, res.getID());
                        res.releaseControl(info.getPid());
                    }
                }
                Logger.log_cpu(String.format("Process %d complete and removed from system.", key));
                String stats_key = String.format("Complete P%d", key);
                Statistics.getStatLog().register(stats_key, cpu.getCycleCount());
                stats_key = String.format("Wait P%d", key);
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

    private void performRead(SimProcessInfo info, int addr){
        int pageNum = info.getPageNum(addr);
        if(!SimRAM.getInstance().contains(info.getPid(), addr))
            Logger.error_mem(String.format("Process %d read from page %d, which is not in memory.", info.getPid(), pageNum));
        SimPage page = SimRAM.getInstance().find(info.getPid(), pageNum);
        page.read(addr);
    }

    private void performWrite(SimProcessInfo info, int addr){
        int pageNum = info.getPageNum(addr);
        if(!SimRAM.getInstance().contains(info.getPid(), addr))
            Logger.error_mem(String.format("Process %d wrote to page %d, which is not in memory.", info.getPid(), pageNum));
        SimPage page = SimRAM.getInstance().find(info.getPid(), pageNum);
        page.write(addr);
    }
}
