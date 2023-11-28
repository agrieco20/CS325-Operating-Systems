/* File: Simulator.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos;

import com.sos.bookkeeping.Logger;
import com.sos.bookkeeping.Statistics;
import com.sos.generator.CentralRandom;
import com.sos.generator.SimUser;
import com.sos.os.*;

import java.util.ArrayList;

public class Simulator {
    private static final int PROGRESS_UPDATE_FREQ = 10;
    private static final int PROGRESS_GRANULARITY = 20;
    public static void main(String[] args) {
        Logger.limit(false, true, false);
        long rngSeed = System.currentTimeMillis();
        CentralRandom.getRNG(rngSeed);
        System.out.println(rngSeed);
        Statistics.getStatLog();
        Statistics.getStatLog().register("RNG Seed", rngSeed);
        Logger.getLog();
        //Create and setup users
        ArrayList<SimUser> users = new ArrayList<>();
        int numUsers = CentralRandom.getRNG().nextInt(20) + 1;
        int totalProcesses = 0;
        for(int i = 0;i < numUsers;i++){
            int userProcesses = CentralRandom.getRNG().nextInt(100) + 1;
            int userPrograms = CentralRandom.getRNG().nextInt(10) + 1;
            users.add(new SimUser(userPrograms, userProcesses));
        }
        for(SimUser user : users){
            totalProcesses += user.remainingProcesses();
        }
        //Setup simulated operating system
        ProcessScheduler ps = new BasicProcessScheduler();
        MemoryManager mm = new VirtualMemoryManager();
        AccessManager am = new BasicResourceManager();
        SimOS operatingSystem = new SimOS(ps, mm, am);
        //Run the operating system step by step
        int currentStep = 0;
        while(!finished(operatingSystem, users)){
            for(SimUser user : users)
                user.nextStep(operatingSystem);
            operatingSystem.run_step();
            if(currentStep % PROGRESS_UPDATE_FREQ == 0) {
                progressUpdate(totalProcesses, users, operatingSystem);
            }
            currentStep += 1;
        }
        Statistics.getStatLog().register("Average Wait Time", operatingSystem.getAvgWait());
        System.out.println("\rSimulation Completed.");
        Statistics.getStatLog().register("Total CPU cycles", operatingSystem.getCPUCycleCount());
        Statistics.destroy();
        Logger.destroy();
    }

    private static boolean finished(SimOS os, ArrayList<SimUser> users){
        for(SimUser user : users){
            if(!user.finished())return false;
        }
        return os.idle();
    }

    private static void progressUpdate(int totalProcesses, ArrayList<SimUser> users, SimOS os){
        StringBuilder display = new StringBuilder("\rProgress: [");
        double currentProcesses = os.currentProcesses();
        for(SimUser user : users)currentProcesses += user.remainingProcesses();
        currentProcesses = PROGRESS_GRANULARITY*(totalProcesses - currentProcesses)/totalProcesses;
        for(int i = 0;i < PROGRESS_GRANULARITY;i++){
            if(i < (int)currentProcesses) {
                display.append('=');
            }
            else if(i == (int)currentProcesses){
                display.append('>');
            }
            else{
                display.append(' ');
            }
        }
        display.append(']');
        System.out.print(display);
    }
}
