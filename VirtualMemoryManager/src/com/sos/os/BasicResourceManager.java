/* File: BasicResourceManager.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 13 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import java.util.HashMap;

public class BasicResourceManager implements AccessManager{
    private static class MutexLock{
        private boolean locked;
        public MutexLock(){
            locked = false;
        }

        public boolean acquire(){
            if(locked) return false;
            locked = true;
            return true;
        }

        public void release(){
            locked = false;
        }
    }

    HashMap<Integer, MutexLock> locks;

    public BasicResourceManager(){
        locks = new HashMap<>();
    }
    @Override
    public void addResource(int resource) {
        locks.put(resource, new MutexLock());
    }

    @Override
    public boolean requestResource(SimProcessInfo info, int resource, boolean write) {
        return locks.get(resource).acquire();
    }

    @Override
    public void releaseResource(SimProcessInfo info, int resource) {
        locks.get(resource).release();
    }
}
