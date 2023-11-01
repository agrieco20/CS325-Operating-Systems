/* File: SimRAM.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A class for handling simulated RAM.
 */


package com.sos.hardware;

import com.sos.bookkeeping.Logger;
import com.sos.os.OSConstants;
import com.sos.os.SimPage;

public class SimRAM {
    //Singleton Variables and Methods
    private static SimRAM instance = null;

    public static SimRAM create(){
        instance = new SimRAM();
        return instance;
    }

    public static SimRAM getInstance(){
        if(instance == null)return create();
        return instance;
    }

    public static void destroy(){
        instance = null;
    }

    //Internals static class
    private static class Frame{
        SimPage contents;
        boolean free;

        public Frame(){
            contents = null;
            free = true;
        }
    }

    //Instance Variables
    private final Frame[] frames;
    private final int pageSize;
    private final int memSize;

    private SimRAM(){
        memSize = OSConstants.ramSize;
        pageSize = OSConstants.pageSize;
        frames = new Frame[memSize/pageSize];
        for(int i = 0;i < frames.length;i++){
            frames[i] = new Frame();
        }
    }

    //Basic Frame Queries
    public int nextFree(){
        for(int i = 0;i < frames.length;i++){
            if(frames[i].free)return i;
        }
        return -1;
    }

    public boolean store(SimPage page, int frame){
        if(frame < 0 || frame >= frames.length)return false;
        if(!frames[frame].free && frames[frame].contents.isChanged())
            Logger.error_mem(String.format("Page %d of process %d not written to disk before being written over.",
                    frames[frame].contents.getPid(), frames[frame].contents.getPagenum()));
        frames[frame].contents = page;
        frames[frame].contents.placed("RAM");
        frames[frame].free = false;
        return true;
    }

    public boolean free(int frame){
        if(frame < 0 || frame >= frames.length)return false;
        if(frames[frame].free)
            Logger.error_mem(String.format("Freed frame %d which was empty.", frame));
        else if(frames[frame].contents.isChanged())
            Logger.error_mem(String.format("Page %d of process %d freed before being written to disk.",
                    frames[frame].contents.getPid(), frames[frame].contents.getPagenum()));
        frames[frame].free = true;
        if(frames[frame].contents != null && !frames[frame].contents.onDisk())
            frames[frame].contents.placed("Nowhere");
        return true;
    }

    public void freeAll(int pid){
        for(Frame frame : frames){
            if(frame.contents.getPid() == pid){
                frame.free = true;
                if(!frame.contents.onDisk())
                    frame.contents.placed("Nowhere");
            }
        }
    }

    public SimPage get(int frame){
        if(frame < 0 || frame > frames.length)return null;
        if(!frames[frame].contents.inRAM())
            Logger.error_mem(String.format("Page in frame %d no in RAM.", frame));
        if(frames[frame].free) {
            Logger.error_mem(String.format("Page in frame %d does not contain page.", frame));
            return null;
        }
        return frames[frame].contents;
    }

    public SimPage find(int pid, int pagenum){
        for(Frame frame : frames){
            if(!frame.free && frame.contents.getPid() == pid && frame.contents.getPagenum() == pagenum)
                return frame.contents;
        }
        return null;
    }

    public boolean contains(int pid, int address){
        for(Frame frame : frames){
            if(!frame.free && frame.contents.getPid() == pid && frame.contents.contains(address))return true;
        }
        return false;
    }

    public int getMemSize(){
        return memSize;
    }

    public int getPageSize(){
        return pageSize;
    }

    public int getFrameCount(){
        return frames.length;
    }
}
