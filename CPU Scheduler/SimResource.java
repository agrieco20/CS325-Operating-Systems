/* File: SimResource.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;

import java.util.HashSet;

public class SimResource {
    private static int nextID = 1;
    private final int resourceID;
    private final HashSet<Integer> controllingProcesses;

    public SimResource(){
        resourceID = SimResource.nextID;
        SimResource.nextID += 1;
        controllingProcesses = new HashSet<>();
    }

    public void releaseControl(int processID){
        if(controllingProcesses.contains(processID)){
            controllingProcesses.remove(processID);
            Logger.getLog().log(String.format("Process %d released resource %d.", processID, resourceID));
        }
        else{
            Logger.getLog().error(String.format("Process %d attempted to release resource %d, which it did not control.",
                                                    processID, resourceID));
        }
    }

    public void addController(int processID){
        if(!controllingProcesses.contains(processID)) {
            Logger.getLog().log(String.format("Process %d now controlling resource %d.", processID, resourceID));
        }
        controllingProcesses.add(processID);
        if(controllingProcesses.size() > 1){
            Logger.getLog().error(String.format("%d processes controlling resource %d.", controllingProcesses.size(), resourceID));
        }
    }

    public boolean hasControl(int processID){
        return controllingProcesses.contains(processID);
    }

    public int getID(){
        return resourceID;
    }
}
