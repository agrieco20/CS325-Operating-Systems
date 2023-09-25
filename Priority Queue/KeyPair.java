/**
 * @author: Anthony Grieco
 * @Date: 9/14/2023
 *
 * Project Description: This project builds a Priority Queue, which can be used to sort any number of elements based on their perceived level of importance. In this particular case, this Priority Queue takes the form of a Max Heap.
 * File Description: The KeyPair class acts as the object that the Max Heap structure will use in order to sort the elements that it's responsible for based on their given importance.
 */

public class KeyPair {
    int key; //Numeric key is assigned in order to indicate the importance of a given value
    int value; //The actual value being assigned a key in order to indicate its importance

    public KeyPair (int key, int value) {
        this.key = key;
        this.value = value;
    }
}