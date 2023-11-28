/**
 * @author: Anthony Grieco
 * @date: 11/25/2023
 *
 * File Description: This file is responsible for creating the object necessary in order to encapsulate enough data about individual pages being added to frames by SAC-SimOS so that the frames the pages are being stored in can be tracked and freed up as necessary.
 *
 * Disclaimer: The SAC-SimOS and all accompanying files except for "VirtualMemoryManager.java" and "FrameTracker.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

public class FrameTracker {
    private int victimPID;
    private int victimPageNum;

    private final int frame;

    public FrameTracker(int frame){
        this.frame = frame;
    }

    public int getVictimPID(){
        return victimPID;
    }
    public void setVictimPID(int pid){
        victimPID = pid;
    }

    public void setVictimPageNum(int pageNum){
        victimPageNum = pageNum;
    }

    public int getVictimPageNum(){
        return victimPageNum;
    }

    public int getFrame(){
        return frame;
    }
}
