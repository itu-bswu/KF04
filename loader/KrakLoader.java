package loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import loader.EdgeData;
import loader.NodeData;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

import graphlib.Graph;

/**
 * This class can load the data files from krak into a graph representation
 * 
 * @author Peter Tiedemann petert@itu.dk
 */
public class KrakLoader {

	/**
	 * Creates a graph from the given data files
	 * 
	 * @param nodeFile Node file
	 * @param edgeFile Edge file
	 * @return Graph
	 * @throws IOException if there is a problem reading data or the files dont exist
	 */
	public static Graph<KrakEdge, KrakNode> graphFromFiles(String nodeFile,
			String edgeFile) throws IOException {
		
		// make sure the machine doesn't lock down while loading
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		// open the file containing the list of nodes
		BufferedReader br = new BufferedReader(new FileReader(nodeFile));

		br.readLine(); // discard names of columns which is the first line

		String line = br.readLine();
		// a count of node entries found in file
		int nodeCount = 0;

		// An array list containing the nodes we find in the file
		ArrayList<KrakNode> nodes = new ArrayList<KrakNode>();

		while (line != null) {
			NodeData nd = new NodeData(line); // parse the node data into a
												// NodeData object
			nodes.add(new KrakNode(nd)); // Make an edge based on the NodeData
			nodeCount++;
			line = br.readLine();
		}
		br.close();

		System.out.println("Adding " + nodes.size() + " nodes to graph");

		// Create a graph on the nodes
		Graph<KrakEdge, KrakNode> graph = new Graph<KrakEdge, KrakNode>(nodes);
		
		nodes = null;

		// Open the file containing the edge entries
		br = new BufferedReader(new FileReader(edgeFile));

		br.readLine(); // again discarding column names
		line = br.readLine();
		int edgeCount = 0;

		while (line != null) {
			
			System.out.println(line);
			
			EdgeData ed = new EdgeData(line); // parse edge entry
			
			System.out.println(line);
			
			KrakEdge edge = new KrakEdge(ed, graph); // create edge
			System.out.println(line);
			graph.addEdge(edge); // add edge to graph
			edgeCount++;
			line = br.readLine();
			System.out.println(line);
		}
		br.close();

		System.out.println("x");
		
		// Clean up interning table (sestoft)
		KrakEdge.clear();
		
		return graph;
	}

	public static void main(String[] args) throws IOException {
		// String dir = "..\\instance\\Valby\\";
		// String dir = "..\\instance\\Charlottenlund\\";
		//String dir = "..\\instance\\Sjaelland\\";
		// String dir = "..\\krakdata\\";
		String dir = "..\\data\\";
		
		Graph<KrakEdge, KrakNode> graph = graphFromFiles(dir
				+ "kdv_node_unload.txt", dir + "kdv_unload.txt");
		System.out.printf("Graph has %d edges%n", graph.getEdgeCount());
		MemoryMXBean mxbean = ManagementFactory.getMemoryMXBean();
		System.out.printf("Heap memory usage: %d MB%n", mxbean
				.getHeapMemoryUsage().getUsed() / (1000000));
		HashSet<KrakEdge> seenBefore = new HashSet<KrakEdge>();
		int count = 0;
		for (List<KrakEdge> edges : graph.getEdges()) {
			for (KrakEdge edge : edges) {
				if (!seenBefore.contains(edge)) {
					seenBefore.add(edge);
					count++;
				}
			}
		}
		System.out.println("Grafen har faktisk " + count + " kanter.");
	}
}
