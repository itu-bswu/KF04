package testClasses;
import static junit.framework.Assert.*;

import graphlib.Edge;
import graphlib.Graph;
import graphlib.Node;
import gui.Line;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.TestCase;

import org.junit.*;

import core.Model;
import dataobjects.KrakNode;

/**
 * Test Class for the model
 * @author mollerhoj3
 *
 */
public class ModelTest extends TestCase{
	private static Model model;
	
	//TODO skal der laves en kunstig graf man kan køre tests på??
	/**
	 * Before Class
	 */
	@BeforeClass public static void BeforeClass() {
		
		System.out.println("Testing model");
		
		model =  new Model();
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
