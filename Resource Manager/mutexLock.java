/**
 * @author: Anthony Grieco
 * @date: 10/26/2023
 *
 * File Description: This file builds a Mutex Lock class, which is responsible for ensuring that processes can only access available resources
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "ResourceManager.java" and "mutexLock.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

import com.sos.os.SimProcessInfo;

import java.util.ArrayList;

public class mutexLock {
    private final int resourceID;

    private boolean available;

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
