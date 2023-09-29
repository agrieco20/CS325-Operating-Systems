/* File: SimRAM.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A class for handling simulated RAM.
 */


package com.sos.hardware;

public class SimRAM {
    //Constants
    private static final int defaultSize = 10*1024; //10KB default ram
    private static final int defaultPageSize = 512; //1/2KB pages

    //Instance Variables
    private int size;
    private int pageSize;
    private PageInfo[] pages;

    public SimRAM(){
        size = defaultSize;
        pageSize = defaultPageSize;
        pages = new PageInfo[size/pageSize];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public SimRAM(int page_size){
        size = defaultSize;
        this.pageSize = page_size;
        pages = new PageInfo[size/this.pageSize];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public SimRAM(int size, int page_size){
        this.size = size;
        this.pageSize = page_size;
        pages = new PageInfo[this.size/this.pageSize];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public int numPages(){
        return pages.length;
    }

    public int nextFree(){
        for(int i = 0;i < pages.length;i++){
            if(!pages[i].allocated)return i;
        }
        return -1;
    }

    public boolean allocate(int page, int pid, int processPage){
        if(page < 0 || page >= pages.length)return false;
        if(pages[page].allocated)return false;
        pages[page].pid = pid;
        pages[page].processPage = processPage;
        pages[page].allocated = true;
        return true;
    }

    public boolean free(int page){
        if(page < 0 || page >= pages.length)return false;
        pages[page].allocated = false;
        return true;
    }

    public void processClear(int pid){
        for(PageInfo page : pages){
            if(page.pid == pid)page.allocated = false;
        }
    }

    public int getPagePid(int page){
        if(page < 0 || page >= pages.length)return -1;
        return pages[page].pid;
    }

    public int getProcessPage(int page){
        if(page < 0 || page >= pages.length)return -1;
        return pages[page].processPage;
    }

    public int getPageSize(){
        return pageSize;
    }

    public boolean retrieve(int pid, int processPage){
        for(PageInfo page : pages){
            if(page.pid == pid && page.processPage == processPage)return true;
        }
        return false;
    }

    public boolean writeTo(int pid, int processPage){
        for(PageInfo page : pages){
            if(page.pid == pid && page.processPage == processPage){
                page.written = true;
                return true;
            }
        }
        return false;
    }

    private class PageInfo {
        public int pid;
        public int processPage;
        public boolean allocated;
        public boolean written;

        public PageInfo(){
            pid = -1;
            processPage = -1;
            allocated = false;
            written = false;
        }
    }
}
