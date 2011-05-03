package testClasses;
import static junit.framework.Assert.*;

import graphlib.Graph;
import gui.Line;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;


import loader.KrakLoader;

import org.junit.*;

import pathfinding.NoPathException;

import utils.Evaluator;
import utils.Properties;

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
	 * To simplify things, we simply count the number of lines, which is 27
	 */
	@Test public void testGetLines() {
		assertEquals(29, model.getLines().size());
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
	 * Testing a continuing path
	 */
	@Test public void testContinuingPath() {
		try {
			/*
			 * Testing the path finding, and the returning of the lines,
			 * by adding more and more to the path,
			 * and count how many road that has been added.
			 * 
			 * It tests the route distance and travel time as well.
			 */
			model.clearPath();
			
			model.findPath(testGraph.getNode(1),testGraph.getNode(3),Evaluator.BIKE);
			assertEquals(1,model.getPath().size());
			assertEquals(0.0035f,model.getRouteDistance()); //This roads length is 3.5m
			assertEquals(1.0f,model.getRouteTime()); //Each roads drivetime is 1 minute
			
			model.findPath(testGraph.getNode(3),testGraph.getNode(2),Evaluator.BIKE);
			assertEquals(2,model.getPath().size());
			assertEquals(0.0045f,model.getRouteDistance()); //This roads length is 1.0m
			assertEquals(2.0f,model.getRouteTime()); //Each roads drivetime is 1 minute
			
			model.findPath(testGraph.getNode(2),testGraph.getNode(4),Evaluator.BIKE);
			assertEquals(4,model.getPath().size());
			assertEquals(0.0105f,model.getRouteDistance());//These roads lengths are 4.0m and 2.0m
			assertEquals(4.0f,model.getRouteTime());//Each roads drivetime is 1 minute
			
			model.findPath(testGraph.getNode(4),testGraph.getNode(7),Evaluator.BIKE);
			assertEquals(7,model.getPath().size());		
			assertEquals(0.0205f,model.getRouteDistance());//These roads lengths are 2.0m ,3m and 5m
			assertEquals(7.0f,model.getRouteTime());//Each roads drivetime is 1 minute
			
			model.findPath(testGraph.getNode(7),testGraph.getNode(9),Evaluator.BIKE);
			assertEquals(8,model.getPath().size());
			assertEquals(0.026f,model.getRouteDistance());//This roads length is 5.5m
			assertEquals(8.0f,model.getRouteTime());//Each roads drivetime is 1 minute
			
		}
		catch (NoPathException e) {
			System.out.println("No path could be found");
		}
	}
	
	/**
	 * Test for an unreachable path
	 */
	@Test public void testUnreachablePath() {
		model.clearPath();
		try {
			model.findPath(testGraph.getNode(1),testGraph.getNode(12),Evaluator.BIKE);
			assert false;
		}
		catch (NoPathException e) {
			System.out.println("Test posetive");
		}
	}
	
	/**
	 * Test for a one-way road
	 */
	@Test public void testOneWayPath() {		
		try {
			model.clearPath();
			model.findPath(testGraph.getNode(8),testGraph.getNode(7),Evaluator.CAR);
			System.out.println("size: "+model.getPath().size());
			assertEquals(1,model.getPath().size());
			
			model.clearPath();
			model.findPath(testGraph.getNode(7),testGraph.getNode(8),Evaluator.CAR);
			System.out.println("size: "+model.getPath().size());
			assertEquals(3,model.getPath().size()); //TODO: The the directed edges doesn't seem to work. I'm not sure why...
		}
		catch (NoPathException e) {
			System.out.println("Test negative");
		}
	}
	
	/**
	 * Test of the evaluator
	 */
	@Test public void testEvaluator() {		
		try {
			model.clearPath();
			model.findPath(testGraph.getNode(10),testGraph.getNode(9),Evaluator.BIKE);
			assertEquals(1,model.getPath().size());
			
			model.clearPath();
			model.findPath(testGraph.getNode(10),testGraph.getNode(9),Evaluator.CAR);
			assertEquals(3,model.getPath().size()); //The car cannot take the sti and must go around
		}
		catch (NoPathException e) {
			System.out.println("Test negative");
		}
	}
	
	private void printPath() {
		for (Line l : model.getPath()) {
			System.out.println(l.name);
		}
	}
	
}
