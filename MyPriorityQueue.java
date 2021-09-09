/**
 * This is the MyPriorityQueue class for TCSS 342C Assignment 2, Compressed Literature.
 */

import java.util.ArrayList;

/**
 * The MyPriorityQueue class creates a priority queue that holds and sorts its contents based on their weight.
 * 
 * @author leejos5
 * @version TCSS 342C Spr2021
 */
public class MyPriorityQueue<Type extends Comparable<Type>> {
    
    /**
     * ArrayList holding the contents of the priority queue.
     */
	private ArrayList<Type> minHeap;

	/**
	 * Constructor for the priority queue, initializing it to an empty list.
	 */
	public MyPriorityQueue() {
		minHeap = new ArrayList<Type>();
	}

	/**
	 * Adds the given item to the list, sorting it ascending order.
	 * 
	 * @param item The item to be added to the list.
	 */
	public void offer(Type item) {
	    int index = size();
		minHeap.add(item);
	 	boolean sorted = false;
		while (!sorted && index != 0) {
			if (item.compareTo(minHeap.get(index - 1)) < 0) {
				swap(index, index - 1);
				index--;
			} else {
				sorted = true;
			}
		}
	}

	/**
	 * Swaps the location of the items at the given indices.
	 * 
	 * @param theIndex The index of the item to be swapped.
	 * @param theOther The index of the other item to be swapped with.
	 */
	private void swap(int theIndex, int theOther) {
		Type otherItem = minHeap.get(theOther);
		minHeap.set(theOther, minHeap.get(theIndex));
		minHeap.set(theIndex, otherItem);
	}

	/**
	 * Returns and removes the first/lowest weight item in the list.
	 * 
	 * @return the lowest weight item in the list.
	 */
	public Type poll() {
		return minHeap.remove(0);
	}

	/**
	 * Returns the size of the list.
	 * 
	 * @return The size.
	 */
	public int size() {
		return minHeap.size();
	}

	/**
	 * Returns true if the list is empty; false otherwise.
	 * 
	 * @return boolean true or false.
	 */
	public boolean isEmpty() {
		return minHeap.size() == 0;
	}

	/**
	 * Returns a string representation of the priority queue.
	 * 
	 * @return the string representation.
	 */
	public String toString() {
	    StringBuilder result = new StringBuilder();
	    result.append("[" + minHeap.get(0));
	    for (int i = 1; i < minHeap.size(); i++) {
	        result.append(", " + minHeap.get(i));
	    }
		return result.append("]").toString();
	}
}
