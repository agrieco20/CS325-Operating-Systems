import java.util.ArrayList;

/**
 * @author: Anthony Grieco
 * @Date: 9/14/2023
 *
 * Project Description: This project builds a Priority Queue, which can be used to sort any number of elements based on their perceived level of importance. In this particular case, this Priority Queue takes the form of a Max Heap.
 * File Description: The Driver class acts as the test (or driver) program to ensure that the Max Heap structure being implemented actually works.
 */
public class Driver {
    public static void main (String [] args){
//        System.out.println("Test"); //Test


//        PriorityQueue Heap1 = new PriorityQueue();
        //Test
//        ArrayList<KeyPair> Heap = new ArrayList<KeyPair>();
        KeyPair pair1 = new KeyPair(7, 1);
        KeyPair pair2 = new KeyPair(9, 2);
        KeyPair pair3 = new KeyPair(8, 3);
//        Heap.add(pair);
//
//        System.out.println(Heap.get(0).value);

        PriorityQueue Heap1 = new PriorityQueue();
        Heap1.push(pair1);
        Heap1.push(pair2);
        Heap1.push(pair3);

        System.out.println(Heap1.pop());

    }
}
