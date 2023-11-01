/* File: SimPage.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 18 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A data structure to simulate a page of data.
 * Notes:
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;

public class SimPage {
    private final int pid;
    private final int pagenum;
    private boolean changed;
    private String location;
    private final int pageSize;

    public SimPage(int pid, int pagenum){
        this.pid = pid;
        this.pagenum = pagenum;
        this.changed = false;
        this.location = "Nowhere";
        this.pageSize = OSConstants.pageSize;
    }

    public boolean contains(int address){
        return (address >= pageSize * pagenum) && (address < pagenum * (pagenum+1));
    }

    public void write(int address){
        if(!contains(address))
            Logger.error_mem(String.format("Process %d page %d does not contain address %d.",
                    pid, pagenum, address));
        else changed = true;
    }

    public void read(int address){
        if(!contains(address))
            Logger.error_mem(String.format("Process %d page %d does not contain address %d.",
                    pid, pagenum, address));
    }

    public boolean inRAM() {
        return location.equalsIgnoreCase("RAM");
    }

    public boolean onDisk() {
        return location.equalsIgnoreCase("DISK");
    }

    public void placed(String location){
        this.location = location;
        if(this.location.equalsIgnoreCase("DISK"))
            changed = false;
    }

    public boolean isChanged(){
        return changed;
    }

    public int getPid(){
        return pid;
    }

    public int getPagenum(){
        return pagenum;
    }
}
