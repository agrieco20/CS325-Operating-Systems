/**
 * @author: Anthony Grieco
 * @Date: 9/14/2023
 *
 * Project Description: This project builds a Priority Queue, which can be used to sort any number of elements based on their perceived level of importance. In this particular case, this Priority Queue takes the form of a Max Heap.
 * File Description: The PriorityQueue class acts as the actual implementation of the Max Heap structure.
 */

import java.util.ArrayList;

public class PriorityQueue {
    static ArrayList<KeyPair> Heap = new ArrayList<KeyPair>(); //Acts as the structure that Heap itself will filter though
    static KeyPair keyNode; //Used to represent the current/newest node added to the Heap. The Key of the keyNode will be compared to the Key of its parent node to see if they should be swapped (based on which element has the higher Key)
    static KeyPair parent; //Used to represent the Parent node of the keyNode (so that their Keys can be compared and their placements swapped if the keyNode's key is higher)
    static KeyPair root; //Indicates which node is at the top of the Heap (has the highest Key value and will be the node that is Popped and whose value will subsequently be returned). It is also stored at the first index of the "Heap" Arraylist
    static KeyPair tempNode; //Acts as a temporary variable so that the "keyNode" and "parent" nodes can be swapped if "keyNode" has a higher Key than its "parent" does

    public PriorityQueue(){
//        ArrayList<KeyPair> Heap = new ArrayList<KeyPair>();
    }

    //The Push Method is responsible for both adding new nodes to the Heap and determining what their exact position within the current Heap will be
    static void push(KeyPair newNode){
        Heap.add(newNode);
        keyNode = newNode;

        //If the Heap currently has no root (and was an empty Heap prior to the new node being added to it), then the newly added node becomes the root.
        if (Heap.size() == 1){
            root = newNode;
        }
        //Otherwise the keyNode's parent is determined
        else{
            parent = Heap.get(((Heap.size() - 1) -1) / 2);
        }

        while (keyNode != root && keyNode.key > parent.key) {
            tempNode = parent;

            parent = keyNode;
            Heap.get(((Heap.size() - 1) -1)/2).key = keyNode.key;
            Heap.get(((Heap.size() - 1) -1)/2).value = keyNode.value;

            keyNode = tempNode;
            Heap.get(Heap.size() - 1).key = tempNode.key;
            Heap.get(Heap.size() - 1).value = tempNode.value;
        }
    }

    static int pop(){
        return Heap.get(0).value;
    }

//    KeyPair pair = new KeyPair(1, 1); //Test
//
//    ArrayList<KeyPair> Heap = new ArrayList<KeyPair>();
//

//    public static void main (String[] args) {
//        System.out.println("Hello world!");
//    }

    //Use an ArrayList in order to easily implement the Max Heap Structure

}