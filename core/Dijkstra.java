package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import utils.Evaluator;

import core.IndexMinPQ;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import graphlib.Edge;
import graphlib.Graph;

/**
 * 
 * @author Emil
 *
 */
public class Dijkstra {

	/**
	 * Find Path
	 * @param G
	 * @param startNode
	 * @param targetNode
	 * @return
	 * @throws NoPathException
	 */
	public static List<KrakEdge> findPath(Graph<KrakEdge,KrakNode> G, KrakNode startNode, KrakNode targetNode, Evaluator<KrakEdge> eval) throws NoPathException{
		HashMap<KrakNode,KrakEdge> edgeTo = new HashMap<KrakNode,KrakEdge>();
		HashMap<KrakNode,Float> distTo = new HashMap<KrakNode,Float>();
		IndexMinPQ<Float> pq = new IndexMinPQ<Float>(G.getNodeCount());

		int visited = 0;

		distTo.put(startNode, 0.0f);
		pq.insert(startNode.getIndex(),0.0f);
		while(!pq.isEmpty()){
			visited++;
			KrakNode cur = G.getNode(pq.delMin());
			Iterator<KrakEdge> edgesOut = G.outGoingEdges(cur);

			while(edgesOut.hasNext()){
				KrakEdge edge = edgesOut.next();

				if(canDrive(edge,cur)){
					//System.out.println("kører ad "+edge.roadname);
					relax(cur,targetNode,edge,distTo,edgeTo, pq, eval);

					if(edge.getOtherEnd(cur) == targetNode && edgeTo.containsKey(targetNode)){
						ArrayList<KrakEdge> list = new ArrayList<KrakEdge>();
						KrakNode path = targetNode;
						while(edgeTo.containsKey(path)){
							KrakEdge cur_edge = edgeTo.get(path);
							list.add(cur_edge);
							path = cur_edge.getOtherEnd(path);
						}
						//System.out.println(visited+" nodes visited in search");

						// this line is for marking every visited Edge (for comparison of A* vs Dijkstra)
						//list.addAll(edgeTo.values());
						return list;
					}
				}
			}
		}

		throw new NoPathException("no path from " + startNode.index + " to " + targetNode.index);
	}

	/**
	 * Tells if a road can be used due to the direction of traffic.
	 * @param edge The edge to check.
	 * @param cur The place "we are" when looking at the direction of the traffic.
	 * @return true if the road can be used, else false.
	 */
	private static boolean canDrive(KrakEdge edge, KrakNode cur) {
		if(edge.direction == Edge.BOTH){
			//System.out.println(edge.roadname+": is both ways");
			return true; 
		}
		if(edge.getStart() == cur && edge.direction == Edge.FORWARD){
			//System.out.println(edge.roadname+" is forward oriented and we are at start");
			return true;
		}
		if(edge.getEnd() == cur && edge.direction == Edge.BACKWARD){

			//System.out.println(edge.roadname+" is backward oriented and we are at the end");
			return true;
		}

		// IMPORTANT: This is a hack, placed here to be able to use the highways
		if(edge.roadname.startsWith("Motorvej")){
			return true;
		}

		// this should never happen, that means our "cur" is not affiliated with the edge
		return false;
	}

	/**
	 * Relax an edge
	 * @param cur
	 * @param target
	 * @param edge
	 * @param distTo
	 * @param edgeTo
	 * @param pq
	 */
	private static void relax(KrakNode cur,KrakNode target,KrakEdge edge, HashMap<KrakNode,Float> distTo, HashMap<KrakNode,KrakEdge> edgeTo, IndexMinPQ<Float> pq, Evaluator<KrakEdge> eval){
		KrakNode other = edge.getOtherEnd(cur);
		float evaluation;
		try {
			evaluation = eval.evaluate(edge);
		} catch (NotPassableException e) {
			return;
		}
		if(!distTo.containsKey(other) || distTo.get(other) > distTo.get(cur) + evaluation){
			float distance = distTo.get(cur) + evaluation;
			distTo.put(other, distance);
			edgeTo.put(other, edge);
			if(pq.contains(other.getIndex())){
				pq.change(other.getIndex(), distance); // + cur.distanceTo(target)
			}else{
				pq.insert(other.getIndex(), distance); // + |--|
			}
		}
	}
}
