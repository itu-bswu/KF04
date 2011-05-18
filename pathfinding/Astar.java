package pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import utils.IndexMinPQ;

import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import graphlib.Graph;

/**
 * The Dijkstra algorithm, modified with A-star, as a static class.
 */
public class Astar {

	/**
	 * Finds the best path between two points with respect to the given Evaluator.
	 * @param G	The graph in which to look for the path.
	 * @param startNode The Node to start searching from.
	 * @param targetNode The Node we search a best path to.
	 * @param eval The evaluator to evaluate the visited Nodes.
	 * @return A list of KrakEdges representing the path from startNode to targetNode.
	 * @throws NoPathException If there is no legal path between the startNode and the targetNode (with respect to the Evaluator).
	 */
	public static List<KrakEdge> findPath(Graph<KrakEdge,KrakNode> G, KrakNode startNode, KrakNode targetNode, Evaluator eval) throws NoPathException{
		HashMap<KrakNode,KrakEdge> edgeTo = new HashMap<KrakNode,KrakEdge>(); //A map with nodes as keys and their edgeTo as values
		HashMap<KrakNode,Float> distTo = new HashMap<KrakNode,Float>(); //A map with nodes as keys and their distTo as values
		IndexMinPQ<Float> pq = new IndexMinPQ<Float>(G.getNodeCount()); //The priority queue in which the distance to startNode indexes the 

		//Insert the first node into both the map and priority queue, with a distance of 0.
		distTo.put(startNode, 0.0f);
		pq.insert(startNode.getIndex(),0.0f);
		
		//While the priority queue is not empty, take the nearest node and relax each of its edges.
		while(!pq.isEmpty()){
			KrakNode cur = G.getNode(pq.delMin());
			
			//If we have found our targetNode, return a list of KrakEdges representing our path to it.
			if(cur == targetNode){
				Stack<KrakEdge> stack = new Stack<KrakEdge>();
				KrakNode path = targetNode;
				while(edgeTo.containsKey(path)){
					KrakEdge cur_edge = edgeTo.get(path);
					stack.add(cur_edge);
					path = cur_edge.getOtherEnd(path);
				}
				return new ArrayList<KrakEdge>(stack);
			}
			
			Iterator<KrakEdge> edgesOut = G.outGoingEdges(cur);

			while(edgesOut.hasNext()){
				KrakEdge edge = edgesOut.next();
				
				relax(cur,targetNode,edge,distTo,edgeTo, pq, eval);
			}
		}

		throw new NoPathException("No path from " + startNode.index + " to " + targetNode.index);
	}

	/**
	 * Relax an edge (add the other end of it to the priority queue)
	 * @param cur 		The node which edges is currently being relaxed
	 * @param target 	The targetNode
	 * @param edge		The edge being relaxed
	 * @param distTo	The map containing nodes and their distances to the startNode
	 * @param edgeTo	The map containing nodes and their edges
	 * @param pq		The priority queue.
	 */
	private static void relax(KrakNode cur,KrakNode target,KrakEdge edge, HashMap<KrakNode,Float> distTo, HashMap<KrakNode,KrakEdge> edgeTo, IndexMinPQ<Float> pq, Evaluator eval){
		
		KrakNode other = edge.getOtherEnd(cur); //The node at the other side of the edge
		
		//Get the evaluation of the edge
		float evaluation;
		try {
			evaluation = eval.evaluate(edge);
		} catch (NotPassableException e) {
			// If the edge cannot be passed, ignore it 
			return;
		}
		
		//If we are visiting the node for the first time, or if we have found a better way to it, insert it to our maps. 
		if(!distTo.containsKey(other) || distTo.get(other) > distTo.get(cur) + evaluation){
			float distance = distTo.get(cur) + evaluation;
			float heurestic = eval.heuristic(cur, target);
			distTo.put(other, distance);
			edgeTo.put(other, edge);
			
			//If the priority queue already contains this node, change its distance value, if not add it.
			if(pq.contains(other.getIndex())){
				pq.change(other.getIndex(), distance + heurestic);
			}else{
				pq.insert(other.getIndex(), distance + heurestic);
			}
		}
	}
}
