import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class contains methods for splitting the huge data set from krak into
 * smaller roadmap data sets.
 * 
 * A smaller instance is created by providing a NodeFilter and an EdgeFilter
 * which chooses which nodes and edges to include. Edges accepted but missing
 * one or more of their end points are also filtered out. Nodes with no edges
 * are also filtered.
 * 
 * KDV# is changed to be continous 1...n over the remaining n nodes, while
 * KDV_ID remains the same so it can be traced back to the original file. FNODE
 * and TNODE is changed to reflect the new KDV#.
 * 
 * @author petert
 * 
 */
public class InstanceCreator {

	public static void createSmallerIstance(String srcNodeFile,
			String srcEdgeFile, String outNodeFile, String outEdgeFile,
			NodeFilter nf, EdgeFilter ef) throws IOException {
		
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

		File outFile = new File(outEdgeFile);
		outFile.getParentFile().mkdirs();
		outFile.createNewFile();

		FileWriter efw = new FileWriter(outFile);

		BufferedReader br = new BufferedReader(new FileReader(srcNodeFile));

		br.readLine(); // skip past names for columns
		String line = br.readLine();

		HashMap<Integer, NodeData> acceptedNodes = new HashMap<Integer, NodeData>();
		int nodeCount = 0;
		int used = 0;
		System.out.println("Scanning nodes");
		while (line != null) {
			NodeData nd = new NodeData(line);
			if (nf == null || nf.accept(nd)) {
				used++;
				acceptedNodes.put(new Integer(nd.KDV), nd);
			}
			nodeCount++;
			line = br.readLine();
		}

		System.out.println("Found " + nodeCount + " nodes in file "
				+ srcNodeFile + " node filter accepted " + used);

		br = new BufferedReader(new FileReader(srcEdgeFile));

		LinkedList<EdgeData> edges = new LinkedList<EdgeData>();
		int edgeCount = 0;
		br.readLine(); // skip past names for columns
		line = br.readLine();

		HashSet<Integer> neededNodes = new HashSet<Integer>();

		System.out.println("Scanning edges");
		while (line != null) {
			EdgeData ed = new EdgeData(line);
			if (ef == null || ef.accept(ed)) {
				Object fo = acceptedNodes.get(ed.FNODE);
				Object to = acceptedNodes.get(ed.TNODE);
				if (to != null && fo != null) {
					neededNodes.add(new Integer(((NodeData) fo).KDV));
					neededNodes.add(new Integer(((NodeData) to).KDV));
					edges.add(ed);
				}
			}
			edgeCount++;
			if (edgeCount % 1000 == 0) {
				System.out.println("Read " + edgeCount + " edges "
						+ edges.size() + " accepted");
			}
			line = br.readLine();
		}
		System.out.println("Found " + edgeCount + " edges after filtering "
				+ edges.size() + " remains");

		HashMap<Integer, Integer> idMapping = new HashMap<Integer, Integer>();
		Iterator<Integer> it = neededNodes.iterator();
		int newID = 0;
		FileWriter nfw = new FileWriter(outNodeFile);
		nfw.write("ARC#,KDV#,KDV-ID,X-COORD,Y-COORD\n");
		while (it.hasNext()) {
			Integer nodeID = it.next();
			NodeData node = acceptedNodes.get(nodeID);
			newID++;
			node.KDV = newID;
			idMapping.put(nodeID, new Integer(newID));
			nfw.write(node.toString() + "\n");
		}
		nfw.close();
		acceptedNodes = null;

		efw.write("FNODE# TNODE# LENGTH DAV_DK# DAV_DK-ID TYP VEJNAVN FROMLEFT TOLEFT FROMRIGHT TORIGHT FROMLEFT_BOGSTAV TOLEFT_BOGSTAV FROMRIGHT_BOGSTA TORIGHT_BOGSTAV V_SOGNENR H_SOGNENR V_POSTNR H_POSTNR KOMMUNENR VEJKODE SUBNET RUTENR FRAKOERSEL ZONE SPEED DRIVETIME ONE_WAY F_TURN T_TURN VEJNR AENDR_DATO TJEK_ID\n");
		Iterator<EdgeData> eit = edges.iterator();
		while (eit.hasNext()) {
			EdgeData ed = eit.next();
			// update node references in edge
			ed.FNODE = idMapping.get(new Integer(ed.FNODE)).intValue();
			ed.TNODE = idMapping.get(new Integer(ed.TNODE)).intValue();
			efw.write(ed.toString() + "\n");
		}
		efw.close();
	}

	/**
	 * Example of how to use the instance creator, this example removes all
	 * edges that are not in the 2500 Zip-code
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		EdgeFilter ef = new EdgeFilter() {
			public boolean accept(EdgeData ed) {
				return 2920 == ed.V_POSTNR || 2920 == ed.H_POSTNR;
				// 1000 <= ed.V_POSTNR
				// && ed.V_POSTNR < 5000
				// && 1000 <= ed.H_POSTNR
				// && ed.H_POSTNR < 5000
				// && !(3700 <= ed.V_POSTNR
				// && ed.V_POSTNR < 3800
				// || 3700 <= ed.H_POSTNR
				// && ed.H_POSTNR < 3800);
			}
		};

		NodeFilter nf = new NodeFilter() {
			public boolean accept(NodeData ed) {
				return true;
			}
		};

		String fromDir = "..\\krakdata\\";
		String toDir = "instance\\Charlottenlund\\";
		InstanceCreator.createSmallerIstance(fromDir + "kdv_node_unload.txt",
				fromDir + "kdv_unload.txt", toDir + "kdv_node_unload.txt",
				toDir + "kdv_unload.txt", nf, ef);

	}

	static interface EdgeFilter {
		public boolean accept(EdgeData ed);
	}

	static interface NodeFilter {
		public boolean accept(NodeData ed);
	}
}
