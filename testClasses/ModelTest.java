package testClasses;

import static org.junit.Assert.*;
import graphlib.Graph;
import gui.Line;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import loader.KrakLoader;
import org.junit.*;
import pathfinding.NoPathException;
import pathfinding.Evaluator;
import utils.Properties;
import core.Model;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import exceptions.NothingCloseException;

/**
 * Test Class for the model
 * 
 * @author Jens Møllerhøj
 */
public class ModelTest {
	private static Model model;
	private static Graph<KrakEdge, KrakNode> testGraph;

	/**
	 * Before Class
	 */
    @BeforeClass public static void onlyOnce() {
    	
    	System.out.println("!");
    	
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
			System.out.println("A problem occured when trying to read test graph - system will now exit");
			System.exit(0);
		}
		
		return graph;
		
	}

	/**
	 * Update Bounds Tests
	 */
	@Test public void testUpdateBounds() {
		//Null
		try {
			model.updateBounds(null);
		}catch (NullPointerException e){return;}
		assertTrue(false);

		//Width negative
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,-1,0));
		}catch (IllegalArgumentException e) {return;}
		assertTrue(false);

		//Height negative
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,0,-1));
		}catch (IllegalArgumentException e) {return;}
		assertTrue(false);

		//Out of bounds
		try {
			model.updateBounds(new Rectangle2D.Double(-10,-10,100,70));
		}catch (IllegalArgumentException e) {
			System.out.println(e);
			return;
		}
		assertTrue(false);
	}

	/**
	 * Test get bounds
	 */
	@Test public void testGetBounds() {
		model.updateBounds(new Rectangle2D.Double(4,5,10,10));
		assertEquals(new Rectangle2D.Double(4,5,10,10), model.getBounds());
	}

	/**					
	 * Test the lines.
	 */
	@Test public void testGetLines() {
		model.updateBounds(model.originalBounds()); //Reset the bounds
		
		for(Line l : model.getLines()) {
			System.out.println(l);
		}
		
		assertEquals(15, model.getLines().size()); //There are 16 lines, but 1 is a sti that we doesn't show on this zoom level.
	}

	/**
	 * Original bounds
	 */
	@Test public void testOriginalBounds() {
		model.updateBounds(model.originalBounds());
		assertEquals(new Rectangle2D.Double(0,0,12000,11500), model.getBounds());
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
			 */
			model.clearPath();

			model.findPath(testGraph.getNode(1),testGraph.getNode(3),Evaluator.BIKE);
			assertEquals(1,model.getPath().size());
			assertEquals(3.5,model.getRouteDistance(),0); //This roads length is 3.5km
			assertEquals(1.0,model.getRouteTime(),0); //Each roads drivetime is 1 minute

			model.findPath(testGraph.getNode(3),testGraph.getNode(2),Evaluator.BIKE);
			assertEquals(2,model.getPath().size());
			assertEquals(4.5,model.getRouteDistance(),0); //This roads length is 1.0km
			assertEquals(2.0,model.getRouteTime(),0); //Each roads drivetime is 1 minute

			model.findPath(testGraph.getNode(2),testGraph.getNode(4),Evaluator.BIKE);
			assertEquals(4,model.getPath().size());
			assertEquals(10.5,model.getRouteDistance(),0);//These roads lengths are 4.0km and 2.0km
			assertEquals(4.0,model.getRouteTime(),0);//Each roads drivetime is 1 minute

			model.findPath(testGraph.getNode(4),testGraph.getNode(7),Evaluator.BIKE);
			assertEquals(7,model.getPath().size());		
			assertEquals(20.5,model.getRouteDistance(),0);//These roads lengths are 2.0km ,3km and 5km
			assertEquals(7.0,model.getRouteTime(),0);//Each roads drivetime is 1 minute

			model.findPath(testGraph.getNode(7),testGraph.getNode(9),Evaluator.BIKE);
			assertEquals(8,model.getPath().size());

			assertEquals(26.0,model.getRouteDistance(),0);//This roads length is 5.5m
			assertEquals(8.0,model.getRouteTime(),0);//Each roads drivetime is 1 minute

		}
		catch (NoPathException e) {
			System.out.println("No path could be found");
			assertTrue(false);
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
			return;
		}
		assertTrue(false);
	}

	/**
	 * Test for a one-way roads
	 */
	@Test public void testOneWayPath() {		
		try {
			model.clearPath();
			model.findPath(testGraph.getNode(8),testGraph.getNode(7),Evaluator.CAR);
			assertEquals(1,model.getPath().size());

			model.clearPath();
			model.findPath(testGraph.getNode(7),testGraph.getNode(8),Evaluator.CAR);
			assertEquals(3,model.getPath().size());

			model.clearPath();
			model.findPath(testGraph.getNode(6),testGraph.getNode(7),Evaluator.CAR);
			assertEquals(1,model.getPath().size());

			model.clearPath();
			model.findPath(testGraph.getNode(7),testGraph.getNode(6),Evaluator.CAR);
			assertEquals(2,model.getPath().size());
		}
		catch (NoPathException e) {
			assertTrue(false);
		}
	}

	/**
	 * Test for a undrivable road
	 */
	@Test public void testUndriveableRoad() {		
		try {
			model.clearPath();
			model.findPath(testGraph.getNode(5),testGraph.getNode(9),Evaluator.CAR);
			assertTrue(model.getPath().size()>1); //Ff is undriveable, therefor the car must go around!

		}
		catch (NoPathException e) {
			assertTrue(false);
		}
	}

	/**
	 * Test of route determined by evaluator
	 */
	@Test public void testRouteEvaluator() {		
		try {
			//Testing that the "sti" is avoided
			model.clearPath();
			model.findPath(testGraph.getNode(10),testGraph.getNode(9),Evaluator.BIKE);
			assertEquals(1,model.getPath().size());

			model.clearPath();
			model.findPath(testGraph.getNode(10),testGraph.getNode(9),Evaluator.CAR);
			assertTrue(model.getPath().size()>1); //The car cannot take the sti and must go around

			//Testing that the car takes the fastest route, while the bicycle takes the shortest
			model.clearPath();
			model.findPath(testGraph.getNode(1),testGraph.getNode(8),Evaluator.BIKE);

			assertEquals(2,model.getPath().size());

			model.clearPath();
			model.findPath(testGraph.getNode(8),testGraph.getNode(1),Evaluator.CAR);
			assertEquals(1,model.getPath().size());
		}
		catch (NoPathException e) {
			assertTrue(false);
		}
	}

	/**
	 * Test clear path
	 */
	@Test public void testClearPath() {
		try {
			model.findPath(testGraph.getNode(1),testGraph.getNode(4));
		} catch (NoPathException e) {
			assertTrue(false);
		}
		assertFalse(model.getPath().size()==0);
		model.clearPath();
		assertTrue(model.getPath().size()==0);
	}

	/**
	 * Test get closest edge
	 */
	@Test public void testGetClosestEdge() {

		try {
			assertEquals("Mm", model.getClosestEdge(new Point2D.Double(10000,10000),2000,Evaluator.ANYTHING).roadname);
			assertEquals("Ii", model.getClosestEdge(new Point2D.Double(5000,8000),2000,Evaluator.ANYTHING).roadname);			
		} catch (NothingCloseException e) {
			assertTrue(false);
		}

		//Here, the search does not go far enough to find the edge
		try {
			model.getClosestEdge(new Point2D.Double(100,100),10,Evaluator.ANYTHING);
		} catch (NothingCloseException e) {
			return;
		}
		assertTrue(false);

	}

	/**
	 * Test get closest node
	 */
	@Test public void testGetClosestNode() {
		try {		
			assertEquals(2, model.getClosestNode(new Point2D.Double(6000,2000),Evaluator.ANYTHING).getIndex());
			assertEquals(6, model.getClosestNode(new Point2D.Double(4000,7000),Evaluator.ANYTHING).getIndex());
			
			//Here, the test must spread out to find the road
			assertEquals(4, model.getClosestNode(new Point2D.Double(-200,-200),Evaluator.ANYTHING).getIndex());
			
			// For the white box tests 3 and 4
			assertEquals(8, model.getClosestNode(new Point2D.Double(10000,8500), Evaluator.ANYTHING).getIndex());
			assertEquals(7, model.getClosestNode(new Point2D.Double(8500,8500), Evaluator.ANYTHING).getIndex());
			
		} catch (NothingCloseException e) {
			assertTrue(false);
		}
	}

	/**
	 * Test the closest roadname
	 */
	@Test public void testGetClosestRoadname() {
		assertEquals("Jj", model.getClosestRoadname(new Point2D.Double(6050,1100)));
		
		assertEquals(" ", model.getClosestRoadname(new Point2D.Double(10000,5000)));
	}

	/**
	 * Test get route distance
	 */
	@Test public void testRouteDistance() {

		model.clearPath();

		//Test no path
		assertEquals(0.0, model.getRouteDistance(),0); 

		//Test some path
		try {
			model.findPath(testGraph.getNode(1),testGraph.getNode(4));
		} catch (NoPathException e) {
			assertTrue(false);
		}
		assertEquals(10.5, model.getRouteDistance(),0); //The lengths of the edges are: 1+2+3,5+4
	}

	/**
	 * Test get route time
	 */
	@Test public void testRouteTime() {
		model.clearPath();

		//Test no path
		assertEquals(0.0, model.getRouteTime(),0);

		//Test some path
		try {
			model.findPath(testGraph.getNode(1),testGraph.getNode(4));
		} catch (NoPathException e) {
			assertTrue(false);
		}

		assertEquals(4.0, model.getRouteTime(),0); //There are four edges, each with a traveltime of 1.

	}
}