/**
 * @author: Anthony Grieco
 * @date: 10/26/2023
 *
 * File Description: This file builds a Mutex Lock class, which is responsible for ensuring that processes can only access available resources
 *
 * Disclaimer: The SAC-SimOS and all accompanying files except for "ResourceManager.java" and "mutexLock.java" (Written by Anthony Grieco) are all the intellectual property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos;

public class mutexLock {
    private final int resourceID;

    private boolean available;

    //Sets the unique identifying value of the resource that the Mutex Lock is limiting access to
    public mutexLock(int resource){
        resourceID = resource;
        available = true;
    }

    //Returns the unique identifying value of the resource the Mutex Lock is limiting access to
    public int getResourceID(){
        return resourceID;
    }

    //Determines whether a resource is currently available
    public boolean resourceStatus(){
        return available;
    }

    //Locks Resource
    public void acquire(){
        available = false;
    }

    //Unlocks Resource
    public void release(){
        available = true;
    }
}