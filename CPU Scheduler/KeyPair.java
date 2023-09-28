/**
 * @author: Anthony Grieco
 * @Date: 9/24/2023
 *
 * File Description: The KeyPair class acts as the object that the Min Heap Priority Queue structure uses in order to sort the elements that it's responsible for based on their priority (jobs with the shortest estimated completion time are processed first).
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "CPU_Scheduler.java", "PriorityQueue.java", and "KeyPair.java" (Written by Anthony Grieco) are all the property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos.os;

public class KeyPair {
    int key; //Estimated Completion Time
    SimProcessInfo process;

    public KeyPair (int key, SimProcessInfo process) {
        this.key = key;
        this.process = process;
    }
}