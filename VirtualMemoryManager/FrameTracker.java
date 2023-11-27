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
