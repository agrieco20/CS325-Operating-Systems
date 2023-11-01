/* File: SimHDD.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 18 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 * Notes:
 */


package com.sos.hardware;

import com.sos.bookkeeping.Logger;
import com.sos.os.OSConstants;
import com.sos.os.SimPage;

public class SimHDD {
    //Singleton methods/variables
    private static SimHDD instance = null;

    public static SimHDD create(){
        if(instance != null)destroy();
        instance = new SimHDD();
        return instance;
    }

    public static SimHDD getInstance(){
        if(instance == null)return create();
        return instance;
    }

    public static void destroy(){
        instance = null;
    }

    //Instance Variables
    private final int size;
    private final int pageSize;
    private final Block[] blocks;

    private SimHDD(){
        size = OSConstants.driveSize;
        pageSize = OSConstants.pageSize;
        blocks = new Block[size/pageSize];
    }

    public boolean store(SimPage page, int block){
        if(block < 0 || block >= blocks.length)return false;
        if(!blocks[block].free)
            Logger.error_mem(String.format("Block %d overwritten while not free.", block));
        blocks[block].contents = page;
        blocks[block].contents.placed("DISK");
        blocks[block].free = false;
        return true;
    }

    public boolean free(int block){
        if(block < 0 || block >= blocks.length)return false;
        if(blocks[block].free)
            Logger.error_mem(String.format("Freed block %d which was empty.", block));
        blocks[block].free = true;
        if(blocks[block].contents != null && !blocks[block].contents.inRAM())
            blocks[block].contents.placed("Nowhere");
        return true;
    }

    public void freeAll(int pid){
        for(Block block : blocks){
            if(block.contents.getPid() == pid){
                block.free = true;
                if(!block.contents.onDisk())
                    block.contents.placed("Nowhere");
            }
        }
    }

    public SimPage get(int block){
        if(block < 0 || block > blocks.length)return null;
        return blocks[block].contents;
    }

    public SimPage find(int pid, int pagenum){
        for(Block block : blocks){
            if(!block.free && block.contents.getPid() == pid && block.contents.getPagenum() == pagenum)
                return block.contents;
        }
        return null;
    }

    public boolean contains(int pid, int address){
        for(Block block : blocks){
            if(!block.free && block.contents.getPid() == pid && block.contents.contains(address))return true;
        }
        return false;
    }

    public int getSize(){
        return size;
    }

    public int getPageSize(){
        return pageSize;
    }

    private class Block {
        public SimPage contents;
        public boolean free;

        public Block(){
            this.contents = null;
            this.free = true;
        }
    }
}
