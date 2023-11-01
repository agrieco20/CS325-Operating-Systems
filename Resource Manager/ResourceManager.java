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

import java.util.ArrayList;

public class ResourceManager implements AccessManager {
    ArrayList<mutexLock> resourceQueue = new ArrayList<>();

    //Adds all of the potential resources that a process could access to a resource queue and assigns each a Mutex Lock
    public void addResource(int resource){
        mutexLock mulock = new mutexLock(resource);
        resourceQueue.add(mulock);
    };

    //Processes check to see if a resource is currently available (if so, the resource is allocated to that process and locked so that no other process may access it)
    public boolean requestResource(SimProcessInfo process, int resource, boolean write){
        boolean requestSuccess = false;

        for (int i = 0; i < resourceQueue.size(); i++){
            if (resource == resourceQueue.get(i).getResourceID() && resourceQueue.get(i).resourceStatus() == true){
                resourceQueue.get(i).acquire();
                return true;
            }
            else if (resource == resourceQueue.get(i).getResourceID() && resourceQueue.get(i).resourceStatus() == false){
                return false;
            }
        }

        return requestSuccess;
    };

    //Releases the resource currently being used by a process so that it can be accessed again by another process
    public void releaseResource(SimProcessInfo process, int resource){
        for(int i = 0; i < resourceQueue.size(); i++){
            if (resourceQueue.get(i).getResourceID() == resource){
                resourceQueue.get(i).release();
            }
        }
    }
}