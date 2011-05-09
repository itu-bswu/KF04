package testClasses;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import junit.framework.TestCase;
import org.junit.Test;
import utils.Direction;
import utils.RectangleMethods;

public class RectangleMethodsTest extends TestCase{

	@Test
	public void testMoveNorth(){
		Rectangle2D.Double move = constructRectangle();
		Rectangle2D.Double compare = constructRectangle();
		double length = 0.5;
		compare.y = 100.0; 
		move = RectangleMethods.move(move, length, Direction.NORTH);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveSouth(){
		Rectangle2D.Double move = constructRectangle();
		Rectangle2D.Double compare = constructRectangle();
		double length = 0.5;
		compare.y = 0; 
		move = RectangleMethods.move(move, length, Direction.SOUTH);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveEast(){
		Rectangle2D.Double move = constructRectangle();
		Rectangle2D.Double compare = constructRectangle();
		double length = 0.5;
		compare.x = 100.0; 
		move = RectangleMethods.move(move, length, Direction.EAST);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveWest(){
		Rectangle2D.Double move = constructRectangle();
		Rectangle2D.Double compare = constructRectangle();
		double length = 0.5;
		compare.x = 0; 
		move = RectangleMethods.move(move, length, Direction.WEST);
		assertEquals(compare, move);
	}
	
	@Test
	public void testFixRatioByInnerRectangle(){
		Rectangle2D.Double inner = constructRectangle();
		Rectangle2D.Double outer = constructRectangle();
		outer.width *= 1.5; outer.height *= 2.5;
		RectangleMethods.fixRatioByInnerRectangle(inner, outer);
		double inner_ratio = inner.width/inner.height;
		double outer_ratio = outer.width/outer.height;
		assertEquals(inner_ratio, outer_ratio);
	}
	
	@Test
	public void testFixRatioByOuterRectangle(){
		Rectangle2D.Double inner = constructRectangle();
		Rectangle2D.Double outer = constructRectangle();
		outer.width *= 1.5; outer.height *= 2.5;
		RectangleMethods.fixRatioByOuterRectangle(inner, outer);
		double inner_ratio = inner.width/inner.height;
		double outer_ratio = outer.width/outer.height;
		assertEquals(inner_ratio, outer_ratio);
	}
	
	@Test
	public void testPoint2DToRectangle(){
		Point2D.Double a = new Point2D.Double(50, 150);
		Point2D.Double b = new Point2D.Double(150, 50);
		Rectangle2D.Double compare = RectangleMethods.point2DToRectangle(a, b);
		assertEquals(compare, constructRectangle());
	}
	
	@Test
	public void testZoomRectangle(){
		Rectangle2D.Double zoom = RectangleMethods.zoomRectangle(0.2, true, constructRectangle());
		Rectangle2D.Double compare = constructRectangle();
		compare.width = 60;
		compare.height = 60;
		compare.x = 70;
		compare.y = 70;
		
		assertTrue(Math.abs(compare.width - zoom.width) <= 1e-6);
		assertTrue(Math.abs(compare.height - zoom.height) <= 1e-6);
		assertTrue(Math.abs(compare.x - zoom.x) <= 1e-6);
		assertTrue(Math.abs(compare.y - zoom.y) <= 1e-6);
		
		zoom = RectangleMethods.zoomRectangle(0.2, false, constructRectangle());
		compare = constructRectangle();
		compare.width = 140;
		compare.height = 140;
		compare.x = 30;
		compare.y = 30;
		
		assertTrue(Math.abs(compare.width - zoom.width) <= 1e-6);
		assertTrue(Math.abs(compare.height - zoom.height) <= 1e-6);
		assertTrue(Math.abs(compare.x - zoom.x) <= 1e-6);
		assertTrue(Math.abs(compare.y - zoom.y) <= 1e-6);
	}
	
	/**
	 * 
	 * @return A rectangle with x,y at 50, 50 and width and height of 100
	 */
	private Rectangle2D.Double constructRectangle(){
		return new Rectangle2D.Double(50, 50, 100, 100);
	}
}
