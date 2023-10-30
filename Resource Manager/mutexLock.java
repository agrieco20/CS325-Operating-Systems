package com.sos;

import com.sos.os.SimProcessInfo;

import java.util.ArrayList;

public class mutexLock {
    private final int resourceID;

    private boolean available;

//    private ArrayList<SimProcessInfo> processQueue = new ArrayList<>();

    //Sets the unique identifying value of the resource that the Semaphore is limiting access to
    public mutexLock(int resource){
        resourceID = resource;
        available = true;
    }

    //Returns the unique identifying value of the resource the Semaphore is limiting access to
    public int getResourceID(){
        return resourceID;
    }

    public boolean resourceStatus(){
        return available;
    }

    public void acquire(){
        available = false;
    }

    public void release(){
        available = true;
    }
}
