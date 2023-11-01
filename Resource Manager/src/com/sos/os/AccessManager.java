package com.sos.os;

public interface AccessManager {
    void addResource(int resource);
    boolean requestResource(SimProcessInfo process, int resource, boolean write);
    void releaseResource(SimProcessInfo process, int resource);
}
