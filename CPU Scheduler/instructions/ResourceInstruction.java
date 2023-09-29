/* File: ResourceInstruction.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os.instructions;

import com.sos.os.SimInstruction;

public class ResourceInstruction implements SimInstruction {

    public static final int RESOURCE_CYCLES = 10;
    private final int address;
    private final int next;
    private final int resourceAccess;

    public ResourceInstruction(int address, int next, int resourceAccess){
        this.address = address;
        this.next = next;
        this.resourceAccess = resourceAccess;
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
        return RESOURCE_CYCLES;
    }

    @Override
    public boolean isResourceInstruction() {
        return true;
    }

    @Override
    public int getResourceAccess() {
        return resourceAccess;
    }

    @Override
    public boolean isMemoryInstruction() {
        return false;
    }

    @Override
    public int getMemoryAccess(){
        return -1;
    }

    @Override
    public boolean isOperationInstruction() {
        return false;
    }
}
