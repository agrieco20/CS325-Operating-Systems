**Project Description:** The entirety of this project simulates a single core Operating System. My specific assignment was to build a CPU Scheduler that was to serve as a gatekeeper in order to ensure that all active processes currently being processed by the SAC-SimOS are scheduled so that the average job in the queue will be waiting to be processed for the shortest amount of time possible. This specific implementation is an adaptation of the "Shortest Job First" method only it is written so that it will preemptively switch between active processes and prioritize processing jobs that have the shortest estimated completion time first.

**Main File that I Wrote Responsible for CPU Scheduling:** "src/com/sos/os/CPU_Scheduler.java"

**Additional Files I Wrote in order to Support my Scheduler:** "src/com/sos/os/PriorityQueue.java" and "src/com/sos/os/KeyPair.java"

**Disclaimer:** The SAC-SimOS and all accompanying files with the exception of "CPU_Scheduler.java", "PriorityQueue.java", and "KeyPair.java" (Written by Anthony Grieco) are all the property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.

**Original SAC-SimOS GitHub Repository:** https://github.com/xLeachimx/SAC-SimOS
