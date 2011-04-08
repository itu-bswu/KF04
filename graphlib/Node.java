package graphlib;

import java.io.Serializable;

/**
 * Node in a graph
 */
public class Node implements Serializable {

	/**
	 * Index of node.
	 */
	public final int index;

	/**
	 * Constructor for objects of class Node.
	 * 
	 * @param index The index of the node.
	 */
	public Node(int index) {
		this.index = index;
	}

	/**
	 * Get index of node.
	 * 
	 * @return Index of node.
	 */
	public final int getIndex() {
		return index;
	}
}
