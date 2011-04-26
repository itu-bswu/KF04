package testClasses;
import static junit.framework.Assert.*;

import graphlib.Edge;
import graphlib.Graph;
import graphlib.Node;
import gui.Line;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.TestCase;
import loader.KrakLoader;

import org.junit.*;

import utils.Properties;
import utils.Stopwatch;

import core.Model;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

/**
 * Test Class for the model
 * @author mollerhoj3
 *
 */
public class ModelTest {
	private static Model model;
	
	/**
	 * Before Class
	 */
    @BeforeClass public static void onlyOnce() {
    	
    	System.out.println("Testing model...");
    	Graph<KrakEdge, KrakNode> testGraph = loadTestGraph();
    	model = new Model(testGraph);
    	System.out.println("TestGraph: " + testGraph);
     }
	
    
    /**
     * load Test graph
     */
	private static Graph<KrakEdge, KrakNode> loadTestGraph() {
		Graph<KrakEdge, KrakNode> graph = null;
		try {
			File dataDir = new File(".", Properties.get("dataDir"));
			graph = KrakLoader.graphFromFiles(
					new File(dataDir, Properties.get("nodeTestFile"))
					.getAbsolutePath(),
					new File(dataDir, Properties.get("edgeTestFile"))
					.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read test graph - system will know exit");
			System.exit(0);
		}

		return graph;
	}
    
    
    
    
	/**
	 * Fake graph tests
	 */
	@Test public void testFakeGraph() {
		
	}
	
	/**
	 * Update Bounds Tests
	 */
	@Test public void testUpdateBoundsNull() {
		
		try {
		model.updateBounds(null);
		}
		catch (NullPointerException e){
			System.out.println(e);
		}
	}
	
	@Test public void testUpdateBoundsWidth() {
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,-1,0));
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	@Test public void testUpdateBoundsHeight() {
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,0,-1));
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	@Test public void testUpdateBoundsOutOfBounds() {
		try {
			model.updateBounds(new Rectangle2D.Double(-10,-10,100,70));			
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Since the graph is empty, we expect an empty set of lines.
	 */
	@Test public void testGetLines() {
		assertEquals(new HashSet<Line>(), model.getLines());
	}
	
	
	/**
	 * Since the graph is empty, we expect an empty set of lines.
	 */
	//TODO vi burde virkelig have en falsk graph
	@Test public void testGetClosestRoad() {
		//assertEquals(" ", model.getClosestRoad(new Point2D.Double(0,0)));
	}
	
}
