package pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import utils.Evaluator;
import utils.IndexMinPQ;

import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import graphlib.Graph;

/**
 * The dijkstra algoritm (A star) as a static class
 */
public class Dijkstra {

	/**
	 * Find Path
	 * @param G	The graph in which to look for the path
	 * @param startNode
	 * @param targetNode
	 * @param eval The evaluator to use
	 * @return A list of KrakEdges representing the path from startNode to targetNode
	 * @throws NoPathException
	 */
	public static List<KrakEdge> findPath(Graph<KrakEdge,KrakNode> G, KrakNode startNode, KrakNode targetNode, Evaluator eval) throws NoPathException{
<<<<<<< HEAD
		HashMap<KrakNode,KrakEdge> edgeTo = new HashMap<KrakNode,KrakEdge>(); //A map with nodes as keys and their edgeTo as values
		HashMap<KrakNode,Float> distTo = new HashMap<KrakNode,Float>(); //A map with nodes as keys and their distTo as values
		IndexMinPQ<Float> pq = new IndexMinPQ<Float>(G.getNodeCount()); //The priorityqueue in which the distance to startNode indexes the 

		//Insert the first node into both the map and priorityqueue, with a distance of 0.
		distTo.put(startNode, 0.0f);
		pq.insert(startNode.getIndex(),0.0f);
		
		//While the priorityqueue is not empty, take the nearest node and relax each of its edges.
=======
		HashMap<KrakNode,KrakEdge> edgeTo = new HashMap<KrakNode,KrakEdge>();
		HashMap<KrakNode,Double> distTo = new HashMap<KrakNode,Double>();
		IndexMinPQ<Double> pq = new IndexMinPQ<Double>(G.getNodeCount());

		int visited = 0;

		distTo.put(startNode, 0.0);
		pq.insert(startNode.getIndex(),0.0);
>>>>>>> 64a14081a839b949f9c39e5f662211886d1604c8
		while(!pq.isEmpty()){
			KrakNode cur = G.getNode(pq.delMin());
			Iterator<KrakEdge> edgesOut = G.outGoingEdges(cur);

			while(edgesOut.hasNext()){
				KrakEdge edge = edgesOut.next();
				
				relax(cur,targetNode,edge,distTo,edgeTo, pq, eval);

				//If we have found our targetNode, return a list of KrakEdges representing our path to it.
				if(edge.getOtherEnd(cur) == targetNode && edgeTo.containsKey(targetNode)){
					ArrayList<KrakEdge> list = new ArrayList<KrakEdge>();
					KrakNode path = targetNode;
					while(edgeTo.containsKey(path)){
						KrakEdge cur_edge = edgeTo.get(path);
						list.add(cur_edge);
						path = cur_edge.getOtherEnd(path);
					}
					return list;
				}
			}
		}

		throw new NoPathException("No path from " + startNode.index + " to " + targetNode.index);
	}

	/**
	 * Relax an edge
	 * @param cur 		The node which edges is currently being relaxed
	 * @param target 	The targetNode
	 * @param edge		The edge being relaxed
	 * @param distTo	The map containing nodes and their distances to the startNode
	 * @param edgeTo	The map containing nodes and their edges
	 * @param pq		The priorityqueue.
	 */
<<<<<<< HEAD
	private static void relax(KrakNode cur,KrakNode target,KrakEdge edge, HashMap<KrakNode,Float> distTo, HashMap<KrakNode,KrakEdge> edgeTo, IndexMinPQ<Float> pq, Evaluator eval){
		
		KrakNode other = edge.getOtherEnd(cur); //The node at the other side of the edge
		
		//Get the evaluation of the edge
		float evaluation;
=======
	//TODO Also add some comments here.
	private static void relax(KrakNode cur,KrakNode target,KrakEdge edge, HashMap<KrakNode,Double> distTo, HashMap<KrakNode,KrakEdge> edgeTo, IndexMinPQ<Double> pq, Evaluator eval){
		KrakNode other = edge.getOtherEnd(cur);
		double evaluation;
>>>>>>> 64a14081a839b949f9c39e5f662211886d1604c8
		try {
			evaluation = eval.evaluate(edge);
		} catch (NotPassableException e) {
			// If the edge cannot be passed, ignore it 
			return;
		}
		
		//If we are visiting the node for the first time, or if we have found a better way to it, insert it to our maps. 
		if(!distTo.containsKey(other) || distTo.get(other) > distTo.get(cur) + evaluation){
			double distance = distTo.get(cur) + evaluation;
			distTo.put(other, distance);
			edgeTo.put(other, edge);
			
			//If the priorityqueue already contains this node, change its distance value, if not add it.
			if(pq.contains(other.getIndex())){
				pq.change(other.getIndex(), distance + eval.heuristic(cur, target)); // + cur.distanceTo(target)
			}else{
				pq.insert(other.getIndex(), distance + eval.heuristic(cur, target)); // + |--|
			}
		}
	}
}
