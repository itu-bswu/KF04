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
import core.NoPathException;
import core.NothingCloseException;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;

/**
 * Test Class for the model
 * @author mollerhoj3
 *
 */
public class ModelTest {
	private static Model model;
	private static Graph<KrakEdge, KrakNode> testGraph;
	
	/**
	 * Before Class
	 */
    @BeforeClass public static void onlyOnce() {
    	testGraph = loadTestGraph();
    	model = new Model(testGraph);
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
	 * Test the lines.
	 * To simplyfy things, we simply count the number of lines, which is 27
	 */
	@Test public void testGetLines() {
		assertEquals(27, model.getLines().size());
	}
	
	/**
	 * Test the closest roadname
	 */
	@Test public void testGetClosestRoadname() {
		assertEquals("Aa", model.getClosestRoadname(new Point2D.Double(0,0)));
		assertEquals("Ee", model.getClosestRoadname(new Point2D.Double(3,3)));
	}
	
	/**
	 * Testing the pathfinder.
	 * Since our metods only returns lines, the only useful thing to do is to count the number of lines returned.
	 */
	@Test public void testPath() {
		try {
			model.findPath(testGraph.getNode(1),testGraph.getNode(10));
		}
		catch (NoPathException e) {
			System.out.println("No path could be found");
		}
		
		assertEquals(3,model.getPath().size());
		
		
	}
}
