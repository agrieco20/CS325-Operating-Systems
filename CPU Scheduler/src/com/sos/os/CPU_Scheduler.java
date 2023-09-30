/**
 * @author: Anthony Grieco
 * @Date: 9/24/2023
 *
 * File Description: This file serves as a gatekeeper in order to ensure that active processes currently being processed by the SAC-SimOS are scheduled so that the average job will be waiting in the queue for the shortest amount of time possible. This specific implementation is an adaptation of the "Shortest Job First" method only it is written so that it will preemptively switch between active processes and prioritize processing jobs that have the shortest estimated completion time first.
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "CPU_Scheduler.java", "PriorityQueue.java", and "KeyPair.java" (Written by Anthony Grieco) are all the property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos.os;

public class CPU_Scheduler implements ProcessScheduler {
    private final PriorityQueue pqueuePrimary = new PriorityQueue(); //Acts as the initial priority queue that new processes are added to in order to be scheduled and later processed by the SAC-SimOS
    private final PriorityQueue pqueueSecondary = new PriorityQueue(); //Acts as priority queue that new processes are initially switched into once they've been processed for 5 CPU Bursts at a time. Once a process has gone through another 5 CPU Bursts once in this queue, they will be swapped back to pqueuePrimary

    private int burstRate = 10; //Used so that after processes with lesser waiting times have been processed, the number of CPU cycles allocated to a given process at a single time is multiplied by 10 so that later processes with longer estimated wait times in the queue will be processed faster in order to prevent the required usage of extra resources from being wasted in order to merely switch these processes between queues (meaning less waiting time and memory overhead for the SAC-SimOS CPU)
    private int CPU_BurstCounter = burstRate; //Used in order to only allocate so many CPU Bursts to each process before moving onto the next process in order to ensure that this algorithm is preemptive and therefore further minimize the average amount of time that jobs are just sitting idly in one of the two queues
    private int remainingBurstCount;

    //Used to determine whether processes should be getting moved from Primary to Secondary or vice versa once their CPU Burst allocation has dropped to zero for that cycle
    private boolean pqPrimaryEmpty;
    private boolean pqSecondaryEmpty;

    private int p_idReturnValue;

    public CPU_Scheduler(){};

    //Immediately sorts and the stores the newly added process based on its estimated completion time
    public void addProcess(SimProcessInfo process){
        KeyPair newProcess = new KeyPair(process.getEstCompTime(), process);
        pqueuePrimary.push(newProcess);
    }

    //Checks the state of the next process to be run, decrements its estimated completion time, and then returns the p_id of that next process
    public int getNextProcess() {
        if (pqueuePrimary.getHeapSize() == 0) {
            pqPrimaryEmpty = true;
            pqSecondaryEmpty = false;
        }
        if (pqueueSecondary.getHeapSize() == 0) {
            pqPrimaryEmpty = false;
            pqSecondaryEmpty = true;
        }
        if (pqueuePrimary.getHeapSize() == 0 && pqueueSecondary.getHeapSize() == 0) {
            return -1;
        }

        if (pqSecondaryEmpty == true) {
            while (pqueuePrimary.getHeapSize() > 0 && pqueuePrimary.getHeapItem(0).process.getState() == SimProcessState.TERMINATED) {
                pqueuePrimary.pop();
                CPU_BurstCounter = burstRate;
            }
            if (CPU_BurstCounter == 0 && pqueueSecondary.getHeapSize() > 0) {
                pqueueSecondary.push(pqueuePrimary.pop());
                CPU_BurstCounter = burstRate;
            }

            if (pqueuePrimary.getHeapSize() != 0){
                if (pqueuePrimary.getHeapItem(0).key != remainingBurstCount){
                    CPU_BurstCounter = burstRate;
                }
                pqueuePrimary.getHeapItem(0).key -= 1;
                CPU_BurstCounter -= 1;
                remainingBurstCount = pqueuePrimary.getHeapItem(0).key;

                p_idReturnValue = pqueuePrimary.getHeapItem(0).process.getPid();
            }
            else{
                pqPrimaryEmpty = true;
            }
        }

        if (pqPrimaryEmpty == true) {
            while (pqueueSecondary.getHeapSize() > 0 && pqueueSecondary.getHeapItem(0).process.getState() == SimProcessState.TERMINATED) {
                pqueueSecondary.pop();
                CPU_BurstCounter = burstRate;
            }
            if (CPU_BurstCounter == 0 && pqueueSecondary.getHeapSize() > 0) {
                pqueueSecondary.push(pqueuePrimary.pop());
                CPU_BurstCounter = burstRate;
            }

            if (pqueueSecondary.getHeapSize() != 0){
                if (pqueueSecondary.getHeapItem(0).key != remainingBurstCount){
                    CPU_BurstCounter = burstRate;
                }

                pqueueSecondary.getHeapItem(0).key -= 1;
                CPU_BurstCounter -= 1;
                remainingBurstCount = pqueueSecondary.getHeapItem(0).key;

                p_idReturnValue = pqueueSecondary.getHeapItem(0).process.getPid();
            }

            else{
                pqSecondaryEmpty = true;
            }
        }
        return p_idReturnValue;
    }
}