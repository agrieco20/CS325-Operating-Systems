package com.sos.os;

public interface AccessManager {
    void addResource(int resource);
    boolean requestResource(int pid, int resource);
    void releaseResource(int pid, int resource);
}
