/* File: MemoryInstruction.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os.instructions;

import com.sos.os.SimInstruction;
import com.sos.os.SimInstructionType;

public class MemoryInstruction implements SimInstruction {
    public static final int MEMORY_CYCLES = 5;
    private final int address;
    private final int next;
    private final int memoryAccess;
    private final boolean write;

    public MemoryInstruction(int address, int next, int memoryAccess, boolean write){
        this.address = address;
        this.next = next;
        this.memoryAccess = memoryAccess;
        this.write = write;
    }

    @Override
    public int getInstructionAddress() {
        return address;
    }

    @Override
    public int getNextInstructionIndex() {
        return next;
    }

    @Override
    public int getCycleCount() {
        return MEMORY_CYCLES;
    }

    public boolean isWrite() {
        return write;
    }

    public int getMemoryAddress(){
        return memoryAccess;
    }

    public SimInstructionType getType(){
        return SimInstructionType.MEMORY;
    }
}
