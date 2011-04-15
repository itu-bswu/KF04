package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import core.IndexMinPQ;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import graphlib.Graph;

public class Dijkstra {

	public static List<KrakEdge> findPath(Graph<KrakEdge,KrakNode> G, KrakNode startNode, KrakNode targetNode ) throws NoPathException{
		HashMap<KrakNode,KrakEdge> edgeTo = new HashMap<KrakNode,KrakEdge>();
		HashMap<KrakNode,Float> distTo = new HashMap<KrakNode,Float>();
		IndexMinPQ<Float> pq = new IndexMinPQ<Float>(G.getNodeCount());
		
		distTo.put(startNode, 0.0f);
		pq.insert(startNode.getIndex(),0.0f);
		while(!pq.isEmpty()){
			KrakNode cur = G.getNode(pq.delMin());
			Iterator<KrakEdge> edgesOut = G.outGoingEdges(cur);

			while(edgesOut.hasNext()){
				KrakEdge edge = edgesOut.next();
				relax(cur,edge,distTo,edgeTo, pq);
				
				if(edge.getOtherEnd(cur) == targetNode){
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
		
		throw new NoPathException("no path from " + startNode.index + " to " + targetNode.index);
	}
	
	private static void relax(KrakNode cur,KrakEdge edge, HashMap<KrakNode,Float> distTo, HashMap<KrakNode,KrakEdge> edgeTo, IndexMinPQ<Float> pq){
		KrakNode other = edge.getOtherEnd(cur);
		
		if(!distTo.containsKey(other) || distTo.get(other) > distTo.get(cur) + edge.length){
			Float distance = distTo.get(cur) + edge.length;
			distTo.put(other, distance);
			edgeTo.put(other, edge);
			pq.insert(other.getIndex(), distance);
		}
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
