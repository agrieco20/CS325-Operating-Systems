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

    private int CPU_BurstCounter = 5; //Used in order to only allocate so many CPU Bursts to each process before moving onto the next process in order to ensure that this algorithm is preemptive and therefore further minimize the average amount of time that jobs are just sitting idly in one of the two queues

    //Used to determine whether processes should be getting moved from Primary to Secondary or vice versa once their CPU Burst allocation has dropped to zero for that cycle
    private boolean pqPrimaryEmpty;
    private boolean pqSecondaryEmpty;

    private int p_idReturnValue;

    private KeyPair queueSwap;

    private int key;
    private SimProcessInfo process;

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
        boolean skipme = false;
        if (pqSecondaryEmpty == true) {
            while (pqueuePrimary.getHeapSize() > 0 && pqueuePrimary.getHeapItem(0).process.getState() == SimProcessState.TERMINATED) {
                pqueuePrimary.pop();
                CPU_BurstCounter = 5;
                if (pqueuePrimary.getHeapSize() == 0) {
                    skipme = true;
                }
            }
//            if (pqueuePrimary.getHeapSize() == 0) {
//                ;
//            }
            if (skipme == true) {
                if (CPU_BurstCounter == 0) {
                    queueSwap = new KeyPair(pqueuePrimary.getHeapItem(0).key, pqueuePrimary.getHeapItem(0).process);

//                key = pqueuePrimary.getHeapItem(0).key;
//                process = pqueuePrimary.getHeapItem(0).process;

//                queueSwap.key = key;
//                queueSwap.process = process;

                    pqueueSecondary.push(queueSwap);
                    pqueuePrimary.pop();

//                pqueueSecondary.push(pqueuePrimary.pop());
                    CPU_BurstCounter = 5;
                }

                //NOTE: NEED TO FIX CPU BURST SO THAT THE LOWEST IT CAN EVER GO IS 0 IN ADDITION TO HAVING IT RESET WHENEVER A PROCESS WITH A LOWER ESTIMATED COMPLETION TIME IS INSERTED BEFORE THE PRIOR PROCESS HAS COMPLETELY FINISHED ITS OWN CPU BURST (in that case just reset the remaining CPU Bursts of that process back to 5 too)

                try { //temporary
                    pqueuePrimary.getHeapItem(0).key -= 1;
                } catch (IndexOutOfBoundsException e) { //temporary
                    System.out.println("error"); //temporary
                }
                ;//temporary

                CPU_BurstCounter -= 1;
                p_idReturnValue = pqueuePrimary.getHeapItem(0).process.getPid();
            }
        }

        if (pqPrimaryEmpty == true) {
            while (pqueueSecondary.getHeapSize() > 0 && pqueueSecondary.getHeapItem(0).process.getState() == SimProcessState.TERMINATED) {
                pqueueSecondary.pop();
                CPU_BurstCounter = 5;
            }
//            if (pqueuePrimary.getHeapSize() == 0 && pqueueSecondary.getHeapSize() == 0) {
//                return -1;
//            }
            if (CPU_BurstCounter == 0) {
                queueSwap = new KeyPair(pqueueSecondary.getHeapItem(0).key, pqueueSecondary.getHeapItem(0).process);

//                key = pqueuePrimary.getHeapItem(0).key;
//                process = pqueuePrimary.getHeapItem(0).process;

//                queueSwap.key = key;
//                queueSwap.process = process;

                pqueuePrimary.push(queueSwap);
                pqueueSecondary.pop();
            }
            pqueueSecondary.getHeapItem(0).key -= 1;
            CPU_BurstCounter -= 1;
            p_idReturnValue = pqueueSecondary.getHeapItem(0).process.getPid();
        }
        return p_idReturnValue;
    }
}