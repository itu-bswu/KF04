package testClasses;
import static junit.framework.Assert.*;

import gui.Line;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

import junit.framework.TestCase;

import org.junit.*;

import core.Model;

/**
 * Test Class for the model
 * @author mollerhoj3
 *
 */
public class ModelTest{
	private static Model model;
	
	//TODO skal der laves en kunstig graf man kan køre tests på??
	/**
	 * Before Class
	 */
	@BeforeClass public static void BeforeClass() {		
		model =  new Model();
	}	
	
	/**
	 * Update Bounds Tests
	 */
	@Test public void UpdateBoundsTestNull() {
		try {
		model.updateBounds(null);
		}
		catch (NullPointerException e){
			System.out.println(e);
		}
	}
	
	@Test public void updateBoundsTestWidth() {
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,-1,0));
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	@Test public void updateBoundsTestHeight() {
		try {
			model.updateBounds(new Rectangle2D.Double(0,0,0,-1));
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	@Test public void updateBoundsTestOutOfBounds() {
		try {
			model.updateBounds(new Rectangle2D.Double(-10,-10,100,70));			
		}catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Since the graph is empty, we expect an empty set of lines.
	 */
	@Test public void getLinesTest() {
		assertEquals(new HashSet<Line>(), model.getLines());
	}
	
	/**
	 * Test Relative point
	 */
	/*
	 TODO kan man teste private metoder? Det ville være ret nice.
	@Test public void relativePointTest() {
		model.updateBounds(new Rectangle2D.Double(0,0,100,100));
		assertEquals();
	}
	*/
	
	/**
	 * Since the graph is empty, we expect an empty set of lines.
	 */
	//TODO vi burde virkelig have en falsk graph
	@Test public void getClosestRoadTest() {
		//assertEquals(" ", model.getClosestRoad(new Point2D.Double(0,0)));
	}
	
}
