package graphlib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * Stores a graph using an adjacency list. The graph can contain any mix of
 * directed and undirected edges.
 * 
 * @author Peter Tiedemann petert@itu.dk
 */

public class Graph<E extends Edge<N>, N extends Node> {
	// sestoft: Changed edge representation from linked list to array list.
	// Important to create them small: 3 resp. 1 items because the average
	// node degree is very low.

	/**
	 * This is the standard adjacency list representation of a graph in
	 * edges[i], all edges leaving the node with index i is stored in an array
	 * list. This means an undirected edge (a,b) can be found in edges[a] and
	 * edges[b], and the Edge object referenced in both is the same
	 */
	private List<List<E>> edges;

	/**
	 * In order to allow traversing the graph against normal edge directions
	 * (for backwards search or similar), this adjacency list stores at
	 * reverse_edges[i] DIRECTED edges ENTERING node i. Undirected edges have no
	 * reverse mapping as they are already stored in both directions.
	 * 
	 * The reverse mapping is stored seperatly to avoid having to iterate over
	 * these edges during normal traversal. If one traveses the graph in
	 * backwards direction, one has to iterate over both the contents of edges
	 * and reverse_edges. This could be avoided by using 3 adjacency lists, one
	 * for undirected edges, ,one for directed edges in the normal direction,
	 * and one storing directed edges backwards.
	 */
	private List<List<E>> reverse_edges;

	/**
	 * An array of Node objects, representing the nodes of the graph
	 */
	private List<N> nodes;

	/**
	 * A counter storing the total number of edges in the graph
	 */
	private int edgeCount = 0;

	/**
	 * Creates an edge-less graph on the Node objects in the array list.
	 */
	public Graph(List<N> nodes) {
		int numNodes = nodes.size();

		// NB: To match the indexes 1,2,... used in the file
		// we leave an empty slot for node 0

		edges = new ArrayList<List<E>>(numNodes + 1);
		reverse_edges = new ArrayList<List<E>>(numNodes + 1);

		for (int i = 0; i <= numNodes + 1; i++) {
			edges.add(new ArrayList<E>(3));
			reverse_edges.add(new ArrayList<E>(1));
		}

		this.nodes = new ArrayList<N>(numNodes + 1);
		this.nodes.add(null);
		for (int i = 1; i <= numNodes; i++) {
			this.nodes.add(nodes.get(i - 1));
		}
	}

	/**
	 * @return Edges
	 */
	public List<List<E>> getEdges() {
		return edges;
	}
	
	public Set<E> getAllEdges(){
		Set<E> all = new HashSet<E>();
		for(List<E> l : edges){
			all.addAll(l);
		}
		
		return all;
	}
	
	/**
	 * @return Reverse edges
	 */
	public List<List<E>> getReverseEdges () {
		return reverse_edges;
	}
	
	public List<N> getNodes () {
		return nodes;
	}

	/**
	 * Returns the total number of edges in the graph (undirected counted once)
	 * 
	 * @return Amount of edges.
	 */
	public int getEdgeCount() {
		return edgeCount;
	}

	/**
	 * Adds edges contained in a collection to the graph
	 * 
	 * @param c
	 *            A collection of Edge objects
	 */
	public void addEdges(Collection<E> c) {
		Iterator<E> it = c.iterator();
		while (it.hasNext()) {
			E e = it.next();
			this.addEdge(e);
		}
	}

	/**
	 * Adds the given edge to the graph
	 * 
	 * @param e Edge to add
	 */
	public void addEdge(E e) {
		if (e.getN1().index >= getEdges().size() || e.getN2().index >= getEdges().size()) {
			throw new IllegalArgumentException(
					"Attempting to add edge to graph, connecting non-existing nodes "
							+ e.getN1().index + " " + e.getN2().index);
		}
		getEdges().get(e.getStart().index).add(e);
		if (e.direction != Edge.BOTH) {
			// we only need reverse mapping for undirected edges
			reverse_edges.get(e.getEnd().index).add(e);
		} else if (e.direction == Edge.BOTH) {
			getEdges().get(e.getEnd().index).add(e);
		}
		edgeCount++;
	}

	/**
	 * Returns the Node object corresponding to the node index given.
	 * 
	 * @param index
	 * @return Node
	 */
	public N getNode(int index) {
		if (nodes.get(index) == null) {
			throw new RuntimeException("No node at " + index);
		}
		return nodes.get(index);
	}

//	/**
//	 * Increases the number of nodes as indicated by newCount old nodes and
//	 * edges are not affected. Does not support decreasing the size of the
//	 * graph.
//	 * 
//	 * @param newCount
//	 */
//	public void increaseNodeCount(int newCount) {
//		if (newCount <= edges.length)
//			return;
//		else {
//			ArrayList[] edges_new = new ArrayList[newCount];
//			ArrayList[] reverse_edges_new = new ArrayList[newCount];
//			for (int i = 0; i < edges.length; i++) {
//				edges_new[i] = edges[i];
//				reverse_edges_new[i] = reverse_edges[i];
//			}
//			edges = edges_new;
//			reverse_edges = reverse_edges_new;
//			Node[] nodes_new = new Node[newCount];
//			for (int i = 0; i < nodes.length; i++) {
//				nodes_new[i] = nodes[i];
//			}
//			nodes = nodes_new;
//			System.gc();
//		}
//	}

	/**
	 * Returns an iterator over all outgoing edges (includes undirected edges)
	 * from the node n
	 * 
	 * @param n Node to get outgoing edges from.
	 * @return Iterator over the outgoing edges.
	 */
	public Iterator<E> outGoingEdges(Node n) {
		return edges.get(n.index).iterator();
	}
	
	/**
	 * Get all unique outgoing edges.
	 * 
	 * @return Set containing all unique outgoing edges.
	 */
	public Set<E> outGoingEdges () {
		Set<E> newEdges = new HashSet<E>();
		for (List<E> l : this.edges) {
			for (E e : l) {
				newEdges.add(e);
			}
		}
		
		return newEdges;
	}

	/**
	 * Returns an iterator over all incoming edges (excludes undirected edges)
	 * 
	 * @param n Node to get incoming edges from.
	 * @return Iterator over the incoming edges.
	 */
	public Iterator<E> incomingEdges(Node n) {
		return reverse_edges.get(n.index).iterator();
	}
	
	/**
	 * Get all unique incoming edges.
	 * 
	 * @return Set containing all unique incoming edges.
	 */
	public Set<E> incomingEdges () {
		Set<E> newEdges = new HashSet<E>();
		for (List<E> l : this.reverse_edges) {
			for (E e : l) {
				newEdges.add(e);
			}
		}
		
		return newEdges;
	}
}
