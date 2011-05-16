package testClasses;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import junit.framework.TestCase;
import org.junit.Test;
import utils.Direction;
import utils.RectangleMethods;

public class RectangleMethodsTest extends TestCase{

	@Test
	public void testNewBounds(){
		Rectangle2D.Double move = null;
		Rectangle2D.Double compare = null;
		double length = 0;
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.5;
		compare.y = 100.0; 
		move = RectangleMethods.newBounds(move, length, Direction.NORTH);
		assertEquals(compare, move);
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.5;
		compare.y = 0; 
		move = RectangleMethods.newBounds(move, length, Direction.SOUTH);
		assertEquals(compare, move);
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.5;
		compare.x = 100.0; 
		move = RectangleMethods.newBounds(move, length, Direction.EAST);
		assertEquals(compare, move);
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.5;
		compare.x = 0; 
		move = RectangleMethods.newBounds(move, length, Direction.WEST);
		assertEquals(compare, move);
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.1;
		compare.x = 40;
		compare.width = 120;
		compare.y = 40;
		compare.height = 120;
		move = RectangleMethods.newBounds(move, length, Direction.OUT);
		assertEquals(compare, move);
		
		move = constructRectangle();
		compare = constructRectangle();
		length = 0.1;
		compare.x = 60;
		compare.width = 80;
		compare.y = 60;
		compare.height = 80;
		move = RectangleMethods.newBounds(move, length, Direction.IN);
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
		Point2D.Double a = null;
		Point2D.Double b = null;
		Rectangle2D.Double compare = null;
		
		//a.x < b.x && b.y < a.y
		a = new Point2D.Double(50, 150);
		b = new Point2D.Double(150, 50);
		compare = RectangleMethods.point2DToRectangle(a, b);
		
		assertEquals(compare, constructRectangle());
		
		//a.x < b.x && a.y < b.y
		a = new Point2D.Double(50, 50);
		b = new Point2D.Double(150, 150);
		compare = RectangleMethods.point2DToRectangle(a, b);
		
		assertEquals(compare, constructRectangle());
		
		//b.x < a.x && b.y < a.y
		a = new Point2D.Double(150, 150);
		b = new Point2D.Double(50, 50);
		compare = RectangleMethods.point2DToRectangle(a, b);
		
		assertEquals(compare, constructRectangle());
		
		//b.x < a.x && a.y < b.y
		a = new Point2D.Double(150, 50);
		b = new Point2D.Double(50, 150);
		compare = RectangleMethods.point2DToRectangle(a, b);
		
		assertEquals(compare, constructRectangle());
	}
	
	@Test
	public void testMouseZoom(){
		Point a = new Point(80, 540); Point b = new Point(480, 240);
		Rectangle view = new Rectangle(0, 0, 800, 600);
		Rectangle2D.Double model = new Rectangle2D.Double(0, 0, 1000, 750);
		
		Rectangle2D.Double compare = new Rectangle2D.Double(100, 75, 500, 375);
		
		assertEquals(compare, RectangleMethods.mouseZoom(a, b, model, view));
	}
	
	/**
	 * 
	 * @return A rectangle with x,y at 50, 50 and width and height of 100
	 */
	private Rectangle2D.Double constructRectangle(){
		return new Rectangle2D.Double(50, 50, 100, 100);
	}
}
