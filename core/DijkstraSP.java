package core;

import java.util.Iterator;
import java.util.Stack;

import graphlib.Graph;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

/*************************************************************************
 *  Compilation:  javac DijkstraSP.java
 *  Execution:    java DijkstraSP V E
 *  Dependencies: EdgeWeightedDigraph.java IndexMinPQ.java Stack.java DirectedEdge.java
 *
 *  Dijkstra's algorithm. Computes the shortest path tree.
 *  Assumes all weights are nonnegative.
 *
 *************************************************************************/


public class DijkstraSP {
   
	private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private KrakEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    public DijkstraSP(Graph<KrakEdge, KrakNode> graph, int s) {
        distTo = new double[graph.getNodeCount()];
        edgeTo = new KrakEdge[graph.getNodeCount()];
        for (int v = 0; v < graph.getNodeCount(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(graph.getNodeCount());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            
            Iterator<KrakEdge> iterator = graph.outGoingEdges(graph.getNode(v));

            while (iterator.hasNext())
                relax(iterator.next());
        }

        // check optimality conditions
        //assert check(graph, s); //TODO remove this when algoritm is sure to work?
    }

    // relax edge e and update pq if changed
    private void relax(KrakEdge e) {
        int v = e.getN1().getIndex(), w = e.getN2().getIndex();
        if (distTo[w] > distTo[v] + e.length) {
            distTo[w] = distTo[v] + e.length;
            edgeTo[w] = e;
            if (pq.contains(w)) pq.change(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    // length of shortest path from s to v
    public double distTo(int v) {
        return distTo[v];
    }

    // is there a path from s to v?
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // shortest path from s to v as an Iterable, null if no such path
    public Iterable<KrakEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<KrakEdge> path = new Stack<KrakEdge>();
        for (KrakEdge e = edgeTo[v]; e != null; e = edgeTo[e.getN1().getIndex()]) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    
    /*
    private boolean check(Graph<KrakEdge, KrakNode> graph, int s) {

        // check that edge weights are nonnegative
        for (KrakEdge e : graph.getAllEdges()) {
            if (e.length < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < graph.getNodeCount(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < graph.getNodeCount(); v++) {
            for (KrakEdge e : graph.outGoingEdges(graph.getNode(v))) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.getNodeCount(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }*/

    
    public static void test(Graph<KrakEdge,KrakNode> graph) {

        // print graph
        System.out.println("Graph");
        System.out.println("--------------");
        System.out.println(graph);


        // run Dijksra's algorithm from vertex 0
        int s = 2;
        DijkstraSP sp = new DijkstraSP(graph, s);
        System.out.println();


        // print shortest path
        System.out.println("Shortest paths from " + s);
        System.out.println("------------------------");
        for (int v = 0; v < graph.getNodeCount(); v++) {
            if (sp.hasPathTo(v)) {
                System.out.println("s: " +s+ ",v: " +v+ "sp.distT0: " + sp.distTo(v));
                for (KrakEdge e : sp.pathTo(v)) {
                    System.out.println(e.roadname);
                }
                System.out.println();
            }
            else {
            	//System.out.println("no path; s:" + s + ",v:"+ v);
            }
        }
    }

}