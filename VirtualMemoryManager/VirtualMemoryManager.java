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
    ArrayList<FrameTracker> frameVictimTrackerLRU = new ArrayList<>();

    FrameTracker mostRecentFrame;
    FrameTracker newPage;

    public void writeRequest(SimProcessInfo info, int addr){
/** 1. Check to see if item being requested to be READ FROM is currently in memory */
        if (SimRAM.getInstance().find(info.getPid(), info.getPageNum(addr)) == null){
            /** If not currently in memory (and Page Fault DOESN'T occur) [nextFree() != -1], */
            if (SimRAM.getInstance().nextFree() != -1){
                /**a. Add Page to next available frame */
                newPage = new FrameTracker(SimRAM.getInstance().nextFree());
                newPage.setVictimPID(info.getPid());
                newPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU.add(0, newPage);
                SimRAM.getInstance().store(info.getPage(addr), SimRAM.getInstance().nextFree());
            }

            /**If not currently in memory (and Page Fault OCCURS) [nextFree() != -1],*/
            else if (SimRAM.getInstance().nextFree() == -1) {
                /**a. Determine Victim Selection using LRU */
                FrameTracker victim = new FrameTracker(frameVictimTrackerLRU.get(frameVictimTrackerLRU.size() - 1).getFrame());

                /**b. Check to see if the data had been changed in the selected frame [using "vFrame.isChanged()"] */
                if(SimRAM.getInstance().get(victim.getFrame()).isChanged() == true){

                    /**a. If isChanged() returns true, write adjusted page out to SimHDD*/
                    SimHDD.getInstance().addStoreBlock(SimRAM.getInstance().get(victim.getFrame()));
                }
                /**c. Overwrite [by first using "free()" and then "store()"] frame chosen through Victim Selection*/
                SimRAM.getInstance().free(victim.getFrame());
                SimRAM.getInstance().store(info.getPage(addr), victim.getFrame());

                //Modify page fault frame tracker so that frame being read from will now be the most recently used frame (and last to be chosen for Victim Selection)
                for(int i = 0; i < frameVictimTrackerLRU.size(); i++) {
                    if(frameVictimTrackerLRU.get(i).getVictimPID() == victim.getVictimPID() && frameVictimTrackerLRU.get(i).getVictimPageNum() == victim.getVictimPageNum()){
                        frameVictimTrackerLRU.remove(i);
                        break;
                    }
                }
                newPage = new FrameTracker(SimRAM.getInstance().nextFree());
                newPage.setVictimPID(info.getPid());
                newPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU.add(0, newPage);
            }
        }

        /** If Page IS currently in Memory, cycle through "frameVictimTrackerLRU" until finding that entry and cycle it to the top of the arraylist to ensure the most recently used frame isn't selected as the next victim frame to be overwritten */
        else{
            for (int i = 0; i < frameVictimTrackerLRU.size(); i++){
                if (frameVictimTrackerLRU.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU.get(i).getVictimPageNum() == info.getPageNum(addr)){
                    mostRecentFrame = new FrameTracker(frameVictimTrackerLRU.get(i).getFrame());
                    mostRecentFrame.setVictimPID(frameVictimTrackerLRU.get(i).getVictimPID());
                    mostRecentFrame.setVictimPageNum(frameVictimTrackerLRU.get(i).getVictimPageNum());

                    frameVictimTrackerLRU.remove(i);
                    frameVictimTrackerLRU.add(0, mostRecentFrame);
                    break;

                    //copy element to temp placeholder
                    //remove element
                    //add placeholder to index 0 of frameVictimTrackerLRU
                }
            }
        }

        /** CHECK IF THERE ARE ANY "NULL" ELEMENTS IN THE "frameVictimTrackerLRU" with for loop and clean out any entries where that data is = null */
//        for (int i = 0; i<frameVictimTrackerLRU.size(); i++){
//            if(SimRAM.getInstance().find(frameVictimTrackerLRU.get(i).getVictimPID(), frameVictimTrackerLRU.get(i).getVictimPageNum()) == null){
//                frameVictimTrackerLRU.remove(i);
//                i--;
//            }
//        }


//        //TEMPORARY (BasicMemoryManager Solution:)
//        SimRAM.getInstance().free(0);
//        SimRAM.getInstance().store(info.getPage(addr), 0);
////        System.out.println(SimRAM.getInstance().getFrameCount());
    }

    public void readRequest(SimProcessInfo info, int addr){

        //1. Check to see if item being requested to be read from is currently in memory
            //If not currently in memory (and Page Fault DOESN'T occur) [nextFree() != -1],
                //a. Add Page to next available frame
            //If not currently in memory (and Page Fault OCCURS) [nextFree() == -1],
                //a. Victim Selection using LRU
                //b. Check to see if the data had been changed in the selected frame [using "info.getPage(addr).isChanged()"]
                    //a. If isChanged() returns true, write adjusted page out to SimHDD
                //c. Overwrite [by first using "free()" and then "store()"] frame chosen through Victim Selection
        //2. Modify page fault frame tracker array so that frame being read from will now be the most recently used frame (adjust array so that the most recently used frame is brought to index 0 and everything that came before where the frame number was originally in the array is shifted down by 1 to that spot in the array)

        //- - - - -

        /** 1. Check to see if item being requested to be READ FROM is currently in memory */
        if (SimRAM.getInstance().find(info.getPid(), info.getPageNum(addr)) == null){
            /** If not currently in memory (and Page Fault DOESN'T occur) [nextFree() != -1], */
            if (SimRAM.getInstance().nextFree() != -1){
                /**a. Add Page to next available frame */
                newPage = new FrameTracker(SimRAM.getInstance().nextFree());
                newPage.setVictimPID(info.getPid());
                newPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU.add(0, newPage);
                SimRAM.getInstance().store(info.getPage(addr), SimRAM.getInstance().nextFree());
            }

            /**If not currently in memory (and Page Fault OCCURS) [nextFree() != -1],*/
            else if (SimRAM.getInstance().nextFree() == -1) {
                /**a. Determine Victim Selection using LRU */
                FrameTracker victim = new FrameTracker(frameVictimTrackerLRU.get(frameVictimTrackerLRU.size() - 1).getFrame());

                /**b. Check to see if the data had been changed in the selected frame [using "vFrame.isChanged()"] */
                if(SimRAM.getInstance().get(victim.getFrame()).isChanged() == true){

                    /**a. If isChanged() returns true, write adjusted page out to SimHDD*/
                    SimHDD.getInstance().addStoreBlock(SimRAM.getInstance().get(victim.getFrame()));
                }
                /**c. Overwrite [by first using "free()" and then "store()"] frame chosen through Victim Selection*/
                SimRAM.getInstance().free(victim.getFrame());
                SimRAM.getInstance().store(info.getPage(addr), victim.getFrame());

                //Modify page fault frame tracker so that frame being read from will now be the most recently used frame (and last to be chosen for Victim Selection)
                for(int i = 0; i < frameVictimTrackerLRU.size(); i++) {
                    if(frameVictimTrackerLRU.get(i).getVictimPID() == victim.getVictimPID() && frameVictimTrackerLRU.get(i).getVictimPageNum() == victim.getVictimPageNum()){
                        frameVictimTrackerLRU.remove(i);
                        break;
                    }
                }
                newPage = new FrameTracker(SimRAM.getInstance().nextFree());
                newPage.setVictimPID(info.getPid());
                newPage.setVictimPageNum(info.getPageNum(addr));

                frameVictimTrackerLRU.add(0, newPage);
            }
        }

        /** If Page IS currently in Memory, cycle through "frameVictimTrackerLRU" until finding that entry and cycle it to the top of the arraylist to ensure the most recently used frame isn't selected as the next victim frame to be overwritten */
        else{
            for (int i = 0; i < frameVictimTrackerLRU.size(); i++){
                if (frameVictimTrackerLRU.get(i).getVictimPID() == info.getPid() && frameVictimTrackerLRU.get(i).getVictimPageNum() == info.getPageNum(addr)){
                    mostRecentFrame = new FrameTracker(frameVictimTrackerLRU.get(i).getFrame());
                    mostRecentFrame.setVictimPID(frameVictimTrackerLRU.get(i).getVictimPID());
                    mostRecentFrame.setVictimPageNum(frameVictimTrackerLRU.get(i).getVictimPageNum());

                    frameVictimTrackerLRU.remove(i);
                    frameVictimTrackerLRU.add(0, mostRecentFrame);
                    break;

                    //copy element to temp placeholder
                    //remove element
                    //add placeholder to index 0 of frameVictimTrackerLRU
                }
            }
        }

        /** CHECK IF THERE ARE ANY "NULL" ELEMENTS IN THE "frameVictimTrackerLRU" with for loop and clean out any entries where that data is = null */
//        for (int i = 0; i<frameVictimTrackerLRU.size(); i++){
//            if(SimRAM.getInstance().find(frameVictimTrackerLRU.get(i).getVictimPID(), frameVictimTrackerLRU.get(i).getVictimPageNum()) == null){
//                frameVictimTrackerLRU.remove(i);
//                i--;
//            }
//        }

//        /**2. Modify page fault frame tracker array so that frame being read from will now be the most recently used frame (adjust array so that the most recently used frame is brought to index 0 and everything that came before where the frame number was originally in the array is shifted down by 1 to that spot in the array)*/
//        System.out.println(info.getPid());
//        System.out.println(info.getPageNum(addr));
//        System.out.println(vFrame.getPid());
//        System.out.println(vFrame.getPagenum());

//        for (int i = 0; i<frameVictimTrackerLRU.length; i++){
//            if (frameVictimTrackerLRU[i]/*.getPid()*/ == /*info.getPid()*/){
////                victimFrame = info.getPid();
////                vFrame = frameVictimTrackerLRU[i];
//                while (i > 0){
//                    frameVictimTrackerLRU[i] = frameVictimTrackerLRU[i-1];
//                    i--;
//                }
////                frameVictimTrackerLRU[0].setVictimPID(info.getPid());
//                frameVictimTrackerLRU[0] = vFrame;
//                break;
//
////                for (int j = 0; j < i; j++){
////
////                }
//            }
//        }
        //        System.out.println(frameVictimTrackerLRU[0]);
//        mostRecentFrame = frameVictimTrackerLRU.get(frameVictimTrackerLRU.size() - 1);
//        frameVictimTrackerLRU.remove(frameVictimTrackerLRU.size() - 1);
//        frameVictimTrackerLRU.add(0, mostRecentFrame);

//        for (int i = frameVictimTrackerLRU.length - 1; i > 0; i--){
//            frameVictimTrackerLRU[i] = frameVictimTrackerLRU[i-1];
//        }
//        frameVictimTrackerLRU[0] = mostRecentFrame;
//        victimFrame vFrame = new victimFrame();

        //TEMPORARY (BasicMemoryManager Solution:)
//        SimRAM.getInstance().free(0);
//        SimRAM.getInstance().store(info.getPage(addr), 0);
////        System.out.println(SimRAM.getInstance().getFrameCount());
    }
}