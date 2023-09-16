/**
 * @author: Anthony Grieco
 * @Date: 9/14/2023
 *
 * Project Description: This project builds a Priority Queue, which can be used to sort any number of elements based on their perceived level of importance. In this particular case, this Priority Queue takes the form of a Max Heap.
 * File Description: The Driver class acts as the test program to ensure that the Max Heap structure being implemented to simulate a Priority Queue actually works.
 */

public class Driver {
    public static void main (String [] args){
        KeyPair pair1 = new KeyPair(7, 1);
        KeyPair pair2 = new KeyPair(9, 2);
        KeyPair pair3 = new KeyPair(8, 3);
        KeyPair pair4 = new KeyPair(10, 4);
        KeyPair pair5 = new KeyPair(1, 5);
        KeyPair pair6 = new KeyPair(9, 6);
        KeyPair pair7 = new KeyPair(3, 7);

        PriorityQueue pqueue = new PriorityQueue();
        pqueue.push(pair1);
        pqueue.push(pair2);
        pqueue.push(pair3);
        pqueue.push(pair4);
        pqueue.push(pair5);
        pqueue.push(pair6);
        pqueue.push(pair7);

        System.out.println(pqueue.pop());
    }
}