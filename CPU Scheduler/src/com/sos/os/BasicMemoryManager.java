/* File: BasicMemoryManager.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 20 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;
import com.sos.hardware.SimRAM;
import java.util.Random;

public class BasicMemoryManager implements MemoryManager{
    private final Random rng;
    public BasicMemoryManager(){
        rng = new Random();
    }
    @Override
    public void requestMemory(int pid, int addr, SimRAM ram) {
        int page = addr / ram.getPageSize();
        for(int i = 0;i < ram.numPages();i++) {
            //Page already in memory
            if(page == ram.getProcessPage(page))return;
        }
        Logger.getLog().log(String.format("Page fault for process %d page %d.", pid, page));
        ram.free(0);
        ram.allocate(0, pid, page);
    }
}
