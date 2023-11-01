/* File: BasicResourceManager.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 13 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

public class BasicResourceManager implements AccessManager{
    @Override
    public void addResource(int resource) {
        return;
    }

    @Override
    public boolean requestResource(SimProcessInfo info, int resource, boolean write) {
        return true;
    }

    @Override
    public void releaseResource(SimProcessInfo info, int resource) {
        return;
    }
}
