/**
 * @author: Anthony Grieco
 * @date: 11/25/2023
 *
 * File Description: This program is responsible for allowing SAC-SimOS to manage its Virtual Memory. Specifically, this file is responsible for ensuring that only the appropriate pages are being stored in memory so that their ability to be read from and written to can be managed by SAC-SimOS.
 *
 * Disclaimer: The SAC-SimOS and all accompanying files except for "VirtualMemoryManager.java" and "FrameTracker.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

import com.sos.hardware.SimHDD;
import com.sos.hardware.SimRAM;
import com.sos.os.MemoryManager;
import com.sos.os.SimProcessInfo;

import java.util.ArrayList;

public class VirtualMemoryManager implements MemoryManager{
    ArrayList<FrameTracker> frameVictimTrackerLRU_RAM = new ArrayList<>();
    ArrayList<FrameTracker> frameVictimTrackerLRU_HDD = new ArrayList<>();

    FrameTracker mostRecentFrame;
    FrameTracker nextPage;

    public void writeRequest(SimProcessInfo info, int addr){
        //Check to see if item being requested to be WRITTEN TO is currently in memory */
        if (SimRAM.getInstance().find(info.getPid(), info.getPageNum(addr)) == null){
            for (int i = 0; i<frameVictimTrackerLRU_HDD.size(); i++){
                if (frameVictimTrackerLRU_HDD.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU_HDD.get(i).getVictimPageNum() == info.getPageNum(addr)){
                    SimHDD.getInstance().free(frameVictimTrackerLRU_HDD.get(i).getFrame());
                    frameVictimTrackerLRU_HDD.remove(i);
                    break;
                }
            }

            //If page is NOT currently in memory and Page Fault DOESN'T occur, add the given page to the next available frame
            if (SimRAM.getInstance().nextFree() != -1){
                nextPage = new FrameTracker(SimRAM.getInstance().nextFree());
                nextPage.setVictimPID(info.getPid());
                nextPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU_RAM.add(0, nextPage);
                SimRAM.getInstance().store(info.getPage(addr), SimRAM.getInstance().nextFree());
            }

            //If page is NOT currently in memory and Page Fault OCCURS, determine which frame will be the victim, check whether the page within that frame has been modified, and then overwrite the frame after writing the changes out to SimHDD as necessary
            else if (SimRAM.getInstance().nextFree() == -1) {
                FrameTracker victim = new FrameTracker(frameVictimTrackerLRU_RAM.get(frameVictimTrackerLRU_RAM.size() - 1).getFrame());

                //Store the frame's contents in specific slot within SimHDD in order to prevent a potentially endless number of blocks being generated in SimHDD
                if(SimRAM.getInstance().get(victim.getFrame()).isChanged() == true){
                    if (SimHDD.getInstance().nextFree() == -1){
                        SimHDD.getInstance().addBlock();

                        FrameTracker oldVictimFrame = new FrameTracker(SimHDD.getInstance().nextFree());
                        frameVictimTrackerLRU_HDD.add(oldVictimFrame);

                        SimHDD.getInstance().store(SimRAM.getInstance().get(victim.getFrame()), SimHDD.getInstance().nextFree());
                    }
                    else {
                        FrameTracker oldVictimFrame = new FrameTracker(SimHDD.getInstance().nextFree());
                        frameVictimTrackerLRU_HDD.add(oldVictimFrame);

                        SimHDD.getInstance().store(SimRAM.getInstance().get(victim.getFrame()), SimHDD.getInstance().nextFree());
                    }
                }
                SimRAM.getInstance().free(victim.getFrame());
                SimRAM.getInstance().store(info.getPage(addr), victim.getFrame());

                //Modify page fault frame tracker so that frame being read from will now be the most recently used frame (and last to be chosen for Victim Selection)
                for(int i = 0; i < frameVictimTrackerLRU_RAM.size(); i++) {
                    if(frameVictimTrackerLRU_RAM.get(i).getVictimPID() == victim.getVictimPID() && frameVictimTrackerLRU_RAM.get(i).getVictimPageNum() == victim.getVictimPageNum()){
                        frameVictimTrackerLRU_RAM.remove(i);
                        break;
                    }
                }
                nextPage = new FrameTracker(SimRAM.getInstance().nextFree());
                nextPage.setVictimPID(info.getPid());
                nextPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU_RAM.add(0, nextPage);
            }
        }

        //If Page IS currently in Memory, find current frame in SimRAM being used and bring it to the top of the "frameVictimTracjerLRU" arraylist so that it won't be immediately selected as the next victim
        else{
            for (int i = 0; i < frameVictimTrackerLRU_RAM.size(); i++){
                if (frameVictimTrackerLRU_RAM.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU_RAM.get(i).getVictimPageNum() == info.getPageNum(addr)){
                    mostRecentFrame = new FrameTracker(frameVictimTrackerLRU_RAM.get(i).getFrame());
                    mostRecentFrame.setVictimPID(frameVictimTrackerLRU_RAM.get(i).getVictimPID());
                    mostRecentFrame.setVictimPageNum(frameVictimTrackerLRU_RAM.get(i).getVictimPageNum());

                    frameVictimTrackerLRU_RAM.remove(i);
                    frameVictimTrackerLRU_RAM.add(0, mostRecentFrame);
                    break;
                }
            }
        }

        //Housekeeping to ensure that only processes still being held in SimHDD are being stored in "frameVictimTrackerLRU_HDD"
        for (int i = 0; i < frameVictimTrackerLRU_HDD.size(); i++){
            if (SimHDD.getInstance().find(frameVictimTrackerLRU_HDD.get(i).getVictimPID(), frameVictimTrackerLRU_HDD.get(i).getVictimPageNum()) == null){
                frameVictimTrackerLRU_HDD.remove(i);
            }
        }
    }

    public void readRequest(SimProcessInfo info, int addr){
        //Check to see if item being requested to be READ FROM is currently in memory
        if (SimRAM.getInstance().find(info.getPid(), info.getPageNum(addr)) == null){
            for (int i = 0; i<frameVictimTrackerLRU_HDD.size(); i++){
                if (frameVictimTrackerLRU_HDD.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU_HDD.get(i).getVictimPageNum() == info.getPageNum(addr)){
                    SimHDD.getInstance().free(frameVictimTrackerLRU_HDD.get(i).getFrame());
                    frameVictimTrackerLRU_HDD.remove(i);
                    break;
                }
            }

            //If page is NOT currently in memory and Page Fault DOESN'T occur, add the given page to the next available frame
            if (SimRAM.getInstance().nextFree() != -1){
                nextPage = new FrameTracker(SimRAM.getInstance().nextFree());
                nextPage.setVictimPID(info.getPid());
                nextPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU_RAM.add(0, nextPage);
                SimRAM.getInstance().store(info.getPage(addr), SimRAM.getInstance().nextFree());
            }

            //If page is NOT currently in memory and Page Fault OCCURS, determine which frame will be the victim, check whether the page within that frame has been modified, and then overwrite the frame after writing the changes out to SimHDD as necessary
            else if (SimRAM.getInstance().nextFree() == -1) {
                FrameTracker victim = new FrameTracker(frameVictimTrackerLRU_RAM.get(frameVictimTrackerLRU_RAM.size() - 1).getFrame());

                //Store the frame's contents in specific slot within SimHDD in order to prevent a potentially endless number of blocks being generated in SimHDD
                if(SimRAM.getInstance().get(victim.getFrame()).isChanged() == true){
                    if (SimHDD.getInstance().nextFree() == -1){
                        SimHDD.getInstance().addBlock();

                        FrameTracker oldVictimFrame = new FrameTracker(SimHDD.getInstance().nextFree());
                        frameVictimTrackerLRU_HDD.add(oldVictimFrame);

                        SimHDD.getInstance().store(SimRAM.getInstance().get(victim.getFrame()), SimHDD.getInstance().nextFree());
                    }
                    else {
                        FrameTracker oldVictimFrame = new FrameTracker(SimHDD.getInstance().nextFree());
                        frameVictimTrackerLRU_HDD.add(oldVictimFrame);

                        SimHDD.getInstance().store(SimRAM.getInstance().get(victim.getFrame()), SimHDD.getInstance().nextFree());
                    }
                }
                SimRAM.getInstance().free(victim.getFrame());
                SimRAM.getInstance().store(info.getPage(addr), victim.getFrame());

                //Modify page fault frame tracker so that frame being read from will now be the most recently used frame (and last to be chosen for Victim Selection)
                for(int i = 0; i < frameVictimTrackerLRU_RAM.size(); i++) {
                    if(frameVictimTrackerLRU_RAM.get(i).getVictimPID() == victim.getVictimPID() && frameVictimTrackerLRU_RAM.get(i).getVictimPageNum() == victim.getVictimPageNum()){
                        frameVictimTrackerLRU_RAM.remove(i);
                        break;
                    }
                }
                nextPage = new FrameTracker(SimRAM.getInstance().nextFree());
                nextPage.setVictimPID(info.getPid());
                nextPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU_RAM.add(0, nextPage);
            }
        }

        //If Page IS currently in Memory, find current frame in SimRAM being used and bring it to the top of the "frameVictimTracjerLRU" arraylist so that it won't be immediately selected as the next victim
        for (int i = 0; i < frameVictimTrackerLRU_RAM.size(); i++){
            if (frameVictimTrackerLRU_RAM.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU_RAM.get(i).getVictimPageNum() == info.getPageNum(addr)){
                mostRecentFrame = new FrameTracker(frameVictimTrackerLRU_RAM.get(i).getFrame());
                mostRecentFrame.setVictimPID(frameVictimTrackerLRU_RAM.get(i).getVictimPID());
                mostRecentFrame.setVictimPageNum(frameVictimTrackerLRU_RAM.get(i).getVictimPageNum());

                frameVictimTrackerLRU_RAM.remove(i);
                frameVictimTrackerLRU_RAM.add(0, mostRecentFrame);
                break;
            }
        }
        //Housekeeping to ensure that only processes still being held in SimHDD are being stored in "frameVictimTrackerLRU_HDD"
        for (int i = 0; i < frameVictimTrackerLRU_HDD.size(); i++){
            if (SimHDD.getInstance().find(frameVictimTrackerLRU_HDD.get(i).getVictimPID(), frameVictimTrackerLRU_HDD.get(i).getVictimPageNum()) == null){
                frameVictimTrackerLRU_HDD.remove(i);
            }
        }
    }
}