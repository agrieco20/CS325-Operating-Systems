/**
 * @author: Anthony Grieco
 * @Date: 9/24/2023
 *
 * File Description: This file constructs a Priority Queue, which is used to sort the jobs in the queue based on which has the shortest estimated completion time and prioritizes them so that they will be scheduled to run on the SAC-SimOS first. This particular Priority Queue takes the form of a Min Heap and the Heapify function is performed through Iteration as opposed to Recursion in order to minimize memory overhead and maximize program efficiency.
 * Note: This Priority Queue structure is used exclusively by Anthony Grieco's "CPU_Scheduler", which intends to minimize the average waiting time of all processes within the queue even at the potential cost of starvation.
 *
 * Disclaimer: The SAC-SimOS and all accompanying files with the exception of "CPU_Scheduler.java", "PriorityQueue.java", and "KeyPair.java" (Written by Anthony Grieco) are all the property of Dr. Michael Andrew Huelsman. Dr. Huelsman published his files on GitHub using a GNU GPLv3 License. All rights reserved by their respective owners.
 * Original SAC-SimOS GitHub Repository: https://github.com/xLeachimx/SAC-SimOS
 */

package com.sos.os;
import java.util.ArrayList;

public class PriorityQueue {
    private ArrayList<KeyPair> Heap = new ArrayList<KeyPair>(); //Acts as the main structure of which the Priority Queue itself will filter though

    private KeyPair keyNode; //Used to represent the current/newest node added to the "Heap". The Key of the keyNode will be compared to the Key of its parent node to see if they should be swapped (based on which element has the lower Key)

    private KeyPair parent; //Used to represent the Parent node of the keyNode (so that their Keys can be compared and their placements swapped if the keyNode's key is lower)

    private KeyPair root; //Indicates which node is at the top of the "Heap" (has the lowest Key value and will be the node that is Popped and whose value will subsequently be returned). It is also stored at the first index of the "Heap" Arraylist

    private int tempNodeKey; //Key: Acts as a temporary variable so that the "keyNode" and "parent" nodes can be swapped if "keyNode" has a lower Key than its "parent" does.

    private SimProcessInfo tempNodeProcess; //Value: Acts as a temporary variable so that the "keyNode" and "parent" nodes can be swapped if "keyNode" has a lower Key than its "parent" does.

    private int p_idx; //Used to keep track of the current Parent Index in the arraylist the "Heap" is currently sorting through in order to determine the new parent-child structure especially once a "Pop" has been initialized and the original "root" has been removed. This element is also used when determining what the next parent-child relationship to be compared will be once the "Push" Method has been called so that a new node can 'move up the tree' of the binary heap structure as much as it possibly can (based on its Key).

    private int c_idx; //Used to keep track of the current Child Index in the arraylist the "Heap" is currently sorting through in order to determine the new parent-child structure once the "Push" Method has been called so that a new node can 'move up the tree' of the binary heap structure as much as it possibly can (based on its Key).

    private KeyPair leftChild; //Used by the "Pop" method to determine whether the current child nodes should in fact be the parent node. If not, the "leftChild" is stored in the "Heap" arraylist at the '(2 * "p_idx") + 1' index where "p_idx" is the current index of the parent node being checked

    private KeyPair rightChild; //Used by the "Pop" method to determine whether the current child nodes should in fact be the parent node. If not, the "rightChild" is stored in the "Heap" arraylist at the '(2 * "p_idx") + 2' index where "p_idx" is the current index of the parent node being checked

    private KeyPair smallest; //Indicates the node in the parent-child nodes currently being looked at that has the smallest Key value

    private int nodeKey; //Used to temporarily store the Key (estimated completion time) of a job being moved to the top of the Priority Queue

    private SimProcessInfo nodeProcess; //Used to temporarily store a job itself that is being moved to the top of the Priority Queue

//    private KeyPair result; //Used in order to output the KeyPair Key and Process being popped from one (Primary) queue so that it can be swapped to the other (Secondary) queue (and vice versa)

    public PriorityQueue(){}

    //The Push Method is responsible for both adding new nodes to the "Heap" and determining what their exact position within the current "Heap" will be (if a child has a lower key value than its parent, the two are then swapped)
    public void push(KeyPair newNode){
        Heap.add(newNode);
        keyNode = newNode;

        if (Heap.size() == 1){
            root = newNode;
        }
        else{
            p_idx = (((Heap.size() - 1) -1)/2);
            c_idx = (Heap.size() - 1);
            parent = Heap.get(p_idx);
        }

        while (keyNode != root && keyNode.key < parent.key) {
            tempNodeKey = parent.key;
            tempNodeProcess = parent.process;

            Heap.get(p_idx).key = keyNode.key;
            Heap.get(p_idx).process = keyNode.process;

            Heap.get(c_idx).key = tempNodeKey;
            Heap.get(c_idx).process = tempNodeProcess;

            c_idx = p_idx;
            p_idx = ((p_idx - 1)/2);
            keyNode = Heap.get(c_idx);
            parent = Heap.get(p_idx);
        }
    }

    //The Pop Method is responsible for removing the "root" (first index) from the "Heap" and then reorganizing the "Heap" structure so that the remaining node with the next lowest key value becomes the new "root"
    public void pop(){
        nodeKey = Heap.get(Heap.size() - 1).key;
        nodeProcess = Heap.get(Heap.size() - 1).process;

//        result.key = Heap.get(0).key;
//        result.key = Heap.get(0).key;
//        result.process = Heap.get(0).process;

        Heap.get(0).key = nodeKey;
        Heap.get(0).process = nodeProcess;

        Heap.remove(Heap.size() - 1);

        p_idx = 0;
        Heapify(p_idx);

//        return result;
    }

    //After being called by the "Pop" Method, the "Heapify" method is responsible for reorganizing the remaining nodes after the original "root" has been removed in order to determine which of them has the lowest Key
    private void Heapify(int p_idx) {
        do {
            try {
                leftChild = Heap.get((2 * p_idx) + 1);
                leftChild = Heap.get((2 * p_idx) + 1);
            } catch (IndexOutOfBoundsException e) {
                return;
            }

            try {
                rightChild = Heap.get((2 * p_idx) + 2);
                rightChild = Heap.get((2 * p_idx) + 2);
            } catch (IndexOutOfBoundsException e) {
                return;
            }

            smallest = Heap.get(p_idx);
            parent = Heap.get(p_idx);

            if (leftChild.key < smallest.key) {
                smallest = leftChild;
            }
            if (rightChild.key < smallest.key) {
                smallest = rightChild;
            }

            if (smallest.key == Heap.get(p_idx).key) {
                return;
            } else {
                tempNodeKey = parent.key;
                tempNodeProcess = parent.process;

                if (smallest == leftChild) {
                    Heap.get(p_idx).key = leftChild.key;
                    Heap.get(p_idx).process = leftChild.process;

                    Heap.get((2 * p_idx) + 1).key = tempNodeKey;
                    Heap.get((2 * p_idx) + 1).process = tempNodeProcess;
                } else {
                    Heap.get(p_idx).key = rightChild.key;
                    Heap.get(p_idx).process = rightChild.process;

                    Heap.get((2 * p_idx) + 2).key = tempNodeKey;
                    Heap.get((2 * p_idx) + 2).process = tempNodeProcess;
                }
                p_idx++;
            }
        } while (smallest.key != Heap.get(p_idx).key);
    }

    //Returns current size of the Heap
    int getHeapSize(){
        return Heap.size();
    }

    //Returns the specific item from the Heap
    KeyPair getHeapItem(int index){
        return Heap.get(index);
    }
}