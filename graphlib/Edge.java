package graphlib;

import java.io.Serializable;

/**
 * Represents an Edge in a graph. Can be both directed an undirected
 * 
 * @author Peter Tiedemann petert@itu.dk
 * 
 */
public class Edge<N extends Node> implements Serializable {
	// indicates the edge goes from v1 -> v2
	public static final byte FORWARD = 1;

	// indicates the edge goes from v2 -> v1
	public static final byte BACKWARD = 2;

	// indicates both of the above(undirected edge)
	public static final byte BOTH = FORWARD | BACKWARD;
	
	protected N n1;
	protected N n2;
	
	/**
	 * The direction of the node.
	 * 
	 * @see FORWARD, BACKWARD and BOTH.
	 */
	protected byte direction;

	protected Edge() {}

	/**
	 * Constructor for objects of class Edge.
	 * 
	 * @param n1 First node.
	 * @param n2 Second node.
	 * @param direction The direction of the edge (FORWARD, BACKWARD, BOTH).
	 */
	public Edge(N n1, N n2, byte direction) {
		this.n1 = n1;
		this.n2 = n2;
		this.direction = direction;
	}

	/**
	 * @return First node
	 */
	public N getN1() {
		return n1;
	}

	/**
	 * @return Second node
	 */
	public N getN2() {
		return n2;
	}

	/**
	 * Returns the start node for a directed edge, otherwise returns an
	 * arbitrary node in the edge
	 * 
	 * @return Node (or subclass) representing the edge-start.
	 */
	public N getStart() {
		if ((direction & FORWARD) != 0) {
			return getN1();
		} else {
			return getN2();
		}
	}

	/**
	 * Returns the node in the edge that is NOT the node n
	 * 
	 * @param n One node of the edge.
	 * @return The other node of the edge.
	 */
	public N getOtherEnd(N n) {
		if (getN1() == n) {
			return getN2();
		} else {
			return getN1();
		}
	}

	/**
	 * Returns the end node for a directed edge, otherwise returns an arbitrary
	 * node in the edge
	 * 
	 * @return One of the nodes.
	 */
	public N getEnd() {
		if ((direction & FORWARD) != 0) {
			return getN2();
		} else {
			return getN1();
		}
	}
}
