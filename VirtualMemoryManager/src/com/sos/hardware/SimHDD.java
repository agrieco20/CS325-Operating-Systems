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
import com.sos.os.SimProcessInfo;

import java.util.ArrayList;

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
    private final ArrayList<Block> blocks;

    private SimHDD(){
        size = OSConstants.driveSize;
        pageSize = OSConstants.pageSize;
        blocks = new ArrayList<>();
    }

    public boolean store(SimPage page, int block){
        if(block < 0 || block >= blocks.size())return false;
        if(!blocks.get(block).free)
            Logger.error_mem(String.format("Block %d overwritten while not free.", block));
        blocks.get(block).contents = page;
        blocks.get(block).contents.placed("DISK");
        blocks.get(block).free = false;
        return true;
    }

    public void addBlock(){
        blocks.add(new Block());
    }

    public void addStoreBlock(SimPage page){
        Block block = new Block(page);
        page.placed("DISK");
        blocks.add(block);
    }

    public boolean free(int block){
        if(block < 0 || block >= blocks.size())return false;
        if(blocks.get(block).free)
            Logger.error_mem(String.format("Freed block %d which was empty.", block));
        blocks.get(block).free = true;
        if(blocks.get(block).contents != null && !blocks.get(block).contents.inRAM())
            blocks.get(block).contents.placed("Nowhere");
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
        if(block < 0 || block > blocks.size())return null;
        return blocks.get(block).contents;
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

    public int getBlockCount(){
        return blocks.size();
    }

    private static class Block {
        public SimPage contents;
        public boolean free;

        public Block(){
            this.contents = null;
            this.free = true;
        }

        public Block(SimPage contents){
            this.contents = contents;
            this.free = false;
        }
    }
}
