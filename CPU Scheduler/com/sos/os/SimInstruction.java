/* File: SimInstruction.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */

package com.sos.os;

public interface SimInstruction {
    int getInstructionAddress();
    int getNextInstructionIndex();
    int getCycleCount();
    boolean isResourceInstruction();
    int getResourceAccess();
    boolean isMemoryInstruction();
    int getMemoryAccess();
    boolean isOperationInstruction();
}
