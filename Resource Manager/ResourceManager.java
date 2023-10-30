/**
 * @author: Anthony Grieco
 * @date: 10/26/2023
 *
 * File Description: This file is responsible for restricting the access that processes have so that their ability to read and write from resources can be managed by SAC-SimOS.
*
* Disclaimer: The SAC-SimOS and all accompanying files with the exception of "ResourceManager.java" and "Semaphore.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
* Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

import com.sos.os.AccessManager;
import com.sos.os.SimProcessInfo;
import com.sos.os.SimProcessState;

import java.util.ArrayList;

public class ResourceManager implements AccessManager {
    ArrayList<Semaphore> resourceQueue = new ArrayList<>();
//    ArrayList<SimProcessInfo> processWaitQueue = new ArrayList<SimProcessInfo>();
    public void addResource(int resource){
        //Need to:
        //1. Create a Semaphore class that takes "resource" as a parameter
        // a. Each resource semaphore is identifiable using its "resource" [just an integer value]
        // b. Needs a counter to indicate how many processes are looking to access that resource
        // c. Needs Wait() Method
        // d. Needs Signal() Method
        //2. Create an arraylist of the ResourceSemaphores so that processes can later check to see if they can use them in the "requestResource" method

        Semaphore sem = new Semaphore(resource);
        resourceQueue.add(sem);
//        if (resource >= 5){
//            System.out.println(resource);
//        }
    };

    public boolean requestResource(SimProcessInfo process, int resource, boolean write){
        //Need to:
        //1. Check to see if the resource the process requesting to access it is actually available (need to use for loop to cycle through the resource arraylist and check for the integer resource value and then to see what the count is. If the count is equal to 0 the resource is available)
        //2. If resource is available [count = 0], return true
        //3. Else, update the semaphore object so it calls wait() [decrementing the count] and then return false

        boolean requestSuccess = false;

        for (int i = 0; i < resourceQueue.size(); i++){
            if (resource == resourceQueue.get(i).getSemID() && resourceQueue.get(i).getSemCount() >= 0){
//                resourceQueue.get(i).semWait(process);
                requestSuccess = true;
                break;
            }
            if(resource == resourceQueue.get(i).getSemID() && resourceQueue.get(i).getSemCount() < 0){
                process.setState(SimProcessState.WAITING);
                resourceQueue.get(i).semWait(process);
//                resourceQueue.get(i).processQueue
                requestSuccess = false;
                break;
            }
        }

////        if (process.getState() == SimProcessState.READY && resourceQueue.size() > 0){
////            return true;
////        }
////        else /* (process.getState() == SimProcessState.READY && resourceQueue.size() < 1) */{
////            processWaitQueue.add(process);
////            return false;
////        }
        return requestSuccess; //Works only with "true" //OLD
//        return true;
    };
    public void releaseResource(SimProcessInfo process, int resource){
        //Need to:
        //1. Stop completed processes from being able to access the resource (remove process from semaphore wait queue)
            //a. Get semaphore object to call signal() and release the resource so that it can then be potentially accessed by other processes again [See textbook for how semaphores work but need to implement that logic here]

        for(int i = 0; i < resourceQueue.size(); i++){
            if (resourceQueue.get(i).getSemID() == resource){
                process.setState(SimProcessState.READY);
                resourceQueue.get(i).semSignal(process);
                break;
            }
        }
    };
}