package com.sos.os;

import com.sos.hardware.SimRAM;

public interface MemoryManager {
    void requestMemory(int pid, int addr, SimRAM ram);
}
