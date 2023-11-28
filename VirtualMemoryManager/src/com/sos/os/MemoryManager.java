package com.sos.os;

import com.sos.hardware.SimRAM;

public interface MemoryManager {
    void writeRequest(SimProcessInfo process, int addr);
    void readRequest(SimProcessInfo process, int addr);
}
