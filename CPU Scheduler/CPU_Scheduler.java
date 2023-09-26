package com.sos.os; /**
 * @author: Anthony Grieco
 * @Date: 9/24/2023
 *
 * File Description: This file serves as a gatekeeper in order to ensure that active processes currently being processed by the SAC-SimOS are scheduled so that the average job will be waiting in the queue for the shortest amount of time possible. This specific implementation is an adaptation of the "Shortest Job First" method only it is written so that it will preemptively switch between active processes and prioritize processing jobs that have the shortest estimated completion time first.
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "com.sos.os.CPU_Scheduler.java", "com.sos.os.PriorityQueue.java", and "com.sos.os.KeyPair.java" (Written by Anthony Grieco) are all the property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective parties.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS.
 */

//import java.util.com.sos.os.PriorityQueue; Use my modified own class instead of this

public class CPU_Scheduler implements ProcessScheduler {
    //WILL DO A "SHORTEST REMAINING TIME FIRST ALGORITHM" (SRTF -> Basically a Shortest Job First Algorithm but is pre-emptive in nature and prioritizes processes being scheduled with the shortest processing time)

    /**
     * Check Doc on Google Drive for Pseudocode to be Followed for each of below methods (delete this comment once com.sos.os.CPU_Scheduler complete)
     *
     * USE THE FULLY FUNCTIONAL "com.sos.os.PriorityQueue.java" CLASS (has successfully been reversed so that it now functions as a MinHeap Priority Queue)
     */

    private final PriorityQueue pqueue = new PriorityQueue();
    public CPU_Scheduler(){};

    //Immediately sorts and the stores the newly added process based on its estimated completion time
    public void addProcess(SimProcessInfo process){
        KeyPair newProcess = new KeyPair(process.getEstCompTime(), process);
        pqueue.push(newProcess);
    }

    //Checks the state of the next process to be run, decrements its estimated completion time, and then returns the p_id of that next process
    public int getNextProcess(){
        while(pqueue.getHeapSize() > 0 && pqueue.getHeapItem(0).process.getState() == SimProcessState.TERMINATED){
            pqueue.pop();
        }
        if (pqueue.getHeapSize() == 0){
            return -1;
        }
        pqueue.getHeapItem(0).key -= 1;
        return pqueue.getHeapItem(0).process.getPid();
    }
}