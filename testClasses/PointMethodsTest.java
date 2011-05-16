package testClasses;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;

import junit.framework.TestCase;
import org.junit.Test;

import utils.PointMethods;

public class PointMethodsTest extends TestCase{
	
	@Test
	public void testPixelToUTM(){
		Point a = new Point(80, 350);
		Rectangle view = new Rectangle(0, 0, 800, 600);
		Rectangle2D.Double model = new Rectangle2D.Double(0, 0, 4000, 3000);
		
		Point compare = new Point(400, 1250);
		
		assertEquals(compare, PointMethods.pixelToUTM(a, model, view));
	}
	
	
	@Test
	public void testUTMToPixel(){
		Rectangle view = new Rectangle(0, 0, 800, 600);
		Rectangle2D.Double model = new Rectangle2D.Double(0, 0, 4000, 3000);
		
		Point compare = new Point(80, 350);
		Point2D.Double a = PointMethods.pixelToUTM(new Point(80, 350), model, view);
		assertEquals(PointMethods.UTMToPixel(a, model, view), compare);
	}
	
	@Test
	public void testPointOutOfBounds(){
		Rectangle view = new Rectangle(0, 0, 800, 600);
		
		Point a = new Point(100, 400);
		Point compare = new Point(100, 400);
		PointMethods.pointOutOfBounds(a, view);
		assertEquals(compare, a);
		
		a = new Point(-100, 400);
		compare = new Point(0, 400); 
		PointMethods.pointOutOfBounds(a, view);
		assertEquals(compare, a);
		
		a = new Point(1200, 400);
		compare = new Point(800, 400); 
		PointMethods.pointOutOfBounds(a, view);
		assertEquals(compare, a);
		
		a = new Point(100, -300);
		compare = new Point(100, 0); 
		PointMethods.pointOutOfBounds(a, view);
		assertEquals(compare, a);
		
		a = new Point(200, 2000);
		compare = new Point(200, 600); 
		PointMethods.pointOutOfBounds(a, view);
		assertEquals(compare, a);
	}
}