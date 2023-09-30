package com.sos.os;

public interface ProcessScheduler {
    void addProcess(SimProcessInfo process);
    int getNextProcess();
}
