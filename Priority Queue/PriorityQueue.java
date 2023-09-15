/**
 * @author: Anthony Grieco
 * @Date: 9/14/2023
 *
 * Project Description: This project builds a Priority Queue, which can be used to sort any number of elements based on their perceived level of importance. In this particular case, this Priority Queue takes the form of a Max Heap.
 * File Description: The PriorityQueue class acts as the actual implementation of the Max Heap structure.
 */

import java.util.ArrayList;

public class PriorityQueue {
    static ArrayList<KeyPair> Heap = new ArrayList<KeyPair>(); //Acts as the main structure of which the Priority Queue itself will filter though
    static KeyPair keyNode; //Used to represent the current/newest node added to the "Heap". The Key of the keyNode will be compared to the Key of its parent node to see if they should be swapped (based on which element has the higher Key)
    static KeyPair parent; //Used to represent the Parent node of the keyNode (so that their Keys can be compared and their placements swapped if the keyNode's key is higher)
    static KeyPair root; //Indicates which node is at the top of the "Heap" (has the highest Key value and will be the node that is Popped and whose value will subsequently be returned). It is also stored at the first index of the "Heap" Arraylist
    static KeyPair tempNode; //Acts as a temporary variable so that the "keyNode" and "parent" nodes can be swapped if "keyNode" has a higher Key than its "parent" does.
    static KeyPair result; //Used to store the node that was previously the "root" before being popped from the "Heap" and the reorganization of the structure that follows just after
    static int p_idx; //Used to keep track of which index in the arraylist the "Heap" is currently sorting through in order to determine the new parent-child structure once a "Pop" has been initialized and the original "root" has been removed
    static KeyPair leftChild; //Used by the "Pop" method to determine whether the current child nodes should in fact be the parent node. If not, the "leftChild" is stored in the "Heap" arraylist at the '(2 * "p_idx") + 1' index where "p_idx" is the current index of the parent node being checked
    static KeyPair rightChild; //Used by the "Pop" method to determine whether the current child nodes should in fact be the parent node. If not, the "rightChild" is stored in the "Heap" arraylist at the '(2 * "p_idx") + 2' index where "p_idx" is the current index of the parent node being checked
    static KeyPair largest; //Indicates the node in the parent-child nodes currently being looked at that has the largest Key value

    public PriorityQueue(){
//        ArrayList<KeyPair> Heap = new ArrayList<KeyPair>();
    }

    //The Push Method is responsible for both adding new nodes to the "Heap" and determining what their exact position within the current "Heap" will be
    static void push(KeyPair newNode){
        Heap.add(newNode);
        keyNode = newNode;

        //If the "Heap" currently has no root (and was an empty "Heap" prior to the new node being added to it), then the newly added node becomes the root.
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

    //The Pop Method is responsible for removing the "root" (first index) from the "Heap" and then reorganizing the "Heap" structure so that the remaining node with the next highest key value becomes the new "root"
    static int pop(){
        result = root;

        Heap.get(0).key = keyNode.key;
        Heap.get(0).value = keyNode.value;

        Heap.remove(Heap.size() - 1);

        p_idx = 0;
        Heapify(p_idx);

        //------
        //TEMPORARY
        for (int i = 0; i < Heap.size(); i++){
            System.out.println(Heap.get(i).value);
        }

        //------

        return result.value; //Heap.get(0).value;
    }

    //After being called by the "Pop" Method, the "Heapify" method is responsible for reorganizing the remaining nodes after the "root" has been removed to determine which of them has the highest Key value
    private static void Heapify(int p_idx){
//        NEED TO FIND A WAY TO PREVENT THE INDEX OUT OF BOUNDS ERROR (try to implement a "Try-Catch" experession before the 4 "leftChild" and "rightChild" statements below)
// *****FOR WHATEVER REASON THE PROGRAM DOESN'T RECOGNIZE "leftChild" or "rightChild" and instead throws an "NullPointerException" (cannot use try-catch to ignore it...need to fix this)

//        try {
            leftChild.key = Heap.get((2 * p_idx) + 1).key;
            leftChild.value = Heap.get((2 * p_idx) + 1).value;
//        }
//        catch(NullPointerException e){
//            return;
//        }

//        try {
            rightChild.key = Heap.get((2 * p_idx) + 2).key;
            rightChild.value = Heap.get((2 * p_idx) + 2).value;
//        }
//        catch(NullPointerException e){
//            return;
//        }
        //---

        largest = Heap.get(p_idx);
        parent = Heap.get(p_idx);

        if (leftChild.key > largest.key){
            largest = leftChild;
        }
        if (rightChild.key > largest.key){
            largest = rightChild;
        }

        if (largest.key == Heap.get(p_idx).key){
            return;
        }
        else{
            tempNode = parent;

            if (largest == leftChild){
                Heap.get(p_idx).key = leftChild.key;
                Heap.get(p_idx).value = leftChild.value;

                Heap.get((2 * p_idx) + 1).key = tempNode.key;
                Heap.get((2 * p_idx) + 1).value = tempNode.value;
            }

            else{ //largest == rightChild
                Heap.get(p_idx).key = rightChild.key;
                Heap.get(p_idx).value = rightChild.value;

                Heap.get((2 * p_idx) + 2).key = tempNode.key;
                Heap.get((2 * p_idx) + 2).value = tempNode.value;
            }
            Heapify(p_idx++);
        }
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