/**
 * @author: Anthony Grieco
 * @date: 10/26/2023
 *
 * File Description: This file is builds a semaphore class, which is responsible for determining how many processes are waiting to access resources and indicates when they are allowed to do so
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "ResourceManager.java" and "Semaphore.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

import com.sos.os.SimProcessInfo;
import com.sos.os.SimProcessState;

import java.util.ArrayList;

public class Semaphore {
    private final int semID;

    private int semCount = 0;

    private ArrayList <SimProcessInfo> processQueue = new ArrayList<>();

    //Sets the unique identifying value of the resource that the Semaphore is limiting access to
    public Semaphore(int resource){
        semID = resource;
    }


    //Returns the unique identifying value of the resource the Semaphore is limiting access to
    public int getSemID(){
        return semID;
    }

    //Returns the count indicating whether a process should be allowed to access a resource
    public int getSemCount(){
        return semCount;
    }

    //Indicates whether a resource is currently being used by a process (and if it is, the new process must wait to access it)
    public void semWait(SimProcessInfo process/*, ArrayList<SimProcessInfo> processWaitQueue*/){
//        semCount--;
        if (semCount <= 0){
            semCount--;
//            //CHANGE PROCESS.READY to PROCESS.WAITING
////            process.setState(SimProcessState.WAITING);
//            processQueue.add(process);
        }
    }

    //Signals that a process is now allowed to access the resource its currently requesting
    public void semSignal(SimProcessInfo process){
        semCount++;


//        if (semCount <= 0){
//            processQueue.remove(process);
//            //CHANGE PROCESS.WAITING to PROCESS.READY
////            process.setState(SimProcessState.READY);
//        }
    }
}