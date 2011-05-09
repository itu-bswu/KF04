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
		Rectangle2D.Float move = constructRectangle();
		Rectangle2D.Float compare = constructRectangle();
		float length = (float) 0.5;
		compare.y = 100.0f; 
		move = RectangleMethods.move(move, length, Direction.NORTH);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveSouth(){
		Rectangle2D.Float move = constructRectangle();
		Rectangle2D.Float compare = constructRectangle();
		float length = (float) 0.5;
		compare.y = 0; 
		move = RectangleMethods.move(move, length, Direction.SOUTH);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveEast(){
		Rectangle2D.Float move = constructRectangle();
		Rectangle2D.Float compare = constructRectangle();
		float length = (float) 0.5;
		compare.x = 100.0f; 
		move = RectangleMethods.move(move, length, Direction.EAST);
		assertEquals(compare, move);
	}
	
	@Test
	public void testMoveWest(){
		Rectangle2D.Float move = constructRectangle();
		Rectangle2D.Float compare = constructRectangle();
		float length = (float) 0.5;
		compare.x = 0; 
		move = RectangleMethods.move(move, length, Direction.WEST);
		assertEquals(compare, move);
	}
	
	@Test
	public void testFixRatioByInnerRectangle(){
		Rectangle2D.Float inner = constructRectangle();
		Rectangle2D.Float outer = constructRectangle();
		outer.width *= 1.5; outer.height *= 2.5;
		RectangleMethods.fixRatioByInnerRectangle(inner, outer);
		float inner_ratio = (float) (inner.width/inner.height);
		float outer_ratio = (float) (outer.width/outer.height);
		assertEquals(inner_ratio, outer_ratio);
	}
	
	@Test
	public void testFixRatioByOuterRectangle(){
		Rectangle2D.Float inner = constructRectangle();
		Rectangle2D.Float outer = constructRectangle();
		outer.width *= 1.5; outer.height *= 2.5;
		RectangleMethods.fixRatioByOuterRectangle(inner, outer);
		float inner_ratio = (float) (inner.width/inner.height);
		float outer_ratio = (float) (outer.width/outer.height);
		assertEquals(inner_ratio, outer_ratio);
	}
	
	@Test
	public void testPoint2DToRectangle(){
		Point2D.Float a = new Point2D.Float(50, 150);
		Point2D.Float b = new Point2D.Float(150, 50);
		Rectangle2D.Float compare = RectangleMethods.point2DToRectangle(a, b);
		assertEquals(compare, constructRectangle());
	}
	
	@Test
	public void testZoomRectangle(){
		Rectangle2D.Float zoom = RectangleMethods.zoomRectangle((float)0.2, true, constructRectangle());
		Rectangle2D.Float compare = constructRectangle();
		compare.width = 60;
		compare.height = 60;
		compare.x = 70;
		compare.y = 70;
		
		assertTrue(Math.abs(compare.width - zoom.width) <= 1e-6);
		assertTrue(Math.abs(compare.height - zoom.height) <= 1e-6);
		assertTrue(Math.abs(compare.x - zoom.x) <= 1e-6);
		assertTrue(Math.abs(compare.y - zoom.y) <= 1e-6);
		
		zoom = RectangleMethods.zoomRectangle((float)0.2, false, constructRectangle());
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
	private Rectangle2D.Float constructRectangle(){
		return new Rectangle2D.Float(50, 50, 100, 100);
	}
}
