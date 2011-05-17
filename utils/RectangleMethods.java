package utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Various methods for manipulating Rectangles for use in our program.
 */
public class RectangleMethods {

	/**
	 * Creates a rectangle moved in a direction compared to the old rectangle.
	 * 
	 * @param old The rectangle to be moved or zoomed.
	 * @param length How far the rectangle should be moved or zoomed.
	 * @param direction Which way should the rectangle move or zoom.
	 * @return The rectangle that has been moved or zoomed. 
	 */
	public static Rectangle2D.Double newBounds(Rectangle2D.Double old, double length, Direction direction){
		switch(direction){
		case WEST:
			double xw = old.x + (-1 * old.width * length);
			return new Rectangle2D.Double(xw, old.y, old.width, old.height);
			
		case EAST:
			double xe = old.x + (1 * old.width * length);
			return new Rectangle2D.Double(xe, old.y, old.width, old.height);
			
		case NORTH:
			double yn = old.y + (1 * old.height * length);
			return new Rectangle2D.Double(old.x, yn, old.width, old.height);
			
		case SOUTH:
			double ys = old.y + (-1 * old.height * length);
			return new Rectangle2D.Double(old.x, ys, old.width, old.height);
			
		case IN:
			return new Rectangle2D.Double(
					old.x + length * old.width, //x is increased by the factor in proportion to the width
					old.y + length * old.height, //y is increased by the factor in proportion to the height
					old.width - (length * old.width * 2), //width is decreased by the factor
					old.height - (length * old.height * 2) //height is decreased by the factor
			);
			
		case OUT:
			return new Rectangle2D.Double(old.x - old.width * length, //x is decreased by the factor in proportion to the width
					old.y - old.height * length, //y is decreased by the factor in proportion to the height
					old.width + old.width * length * 2, //width is increased by the factor
					old.height + old.height * length * 2//height is increased by the factor
			);
		}
		
		return old;
	}

	/**
	 * Adjusts a Rectangle to have the same ratio as another Rectangle by removing excess space (Inner Rectangle).
	 * @param a The Rectangle to adjust.
	 * @param b The Rectangle that has the desired ratio.
	 */
	public static void fixRatioByInnerRectangle(Rectangle2D.Double inner, Rectangle2D.Double outer){
		double outer_ratio = (outer.width / outer.height);
		double inner_ratio = (inner.width / inner.height);

		if(inner_ratio < outer_ratio){
			// cut height

			double temp = inner.height;	
			inner.height = inner.width / outer_ratio;
			inner.y = inner.y - (inner.height - temp) / 2;
		}else{
			// cut width
			double temp = inner.width;
			inner.width = outer_ratio * inner.height;
			inner.x = inner.x - (inner.width - temp) / 2;
		}
	}

	/**
	 * Adjusts a Rectangle to have the same ratio as another Rectangle by adding more (Outer Rectangle)
	 * 
	 * @param a The Rectangle to adjust.
	 * @param b The Rectangle that has the desired ratio.
	 */
	public static void fixRatioByOuterRectangle(Rectangle2D.Double inner, Rectangle2D.Double outer){
		double outer_ratio = (outer.width / outer.height);
		double inner_ratio = (inner.width / inner.height);

		if(inner_ratio < outer_ratio){
			// make wider
			double temp = inner.width;
			inner.width = outer_ratio * inner.height;
			inner.x = inner.x - (inner.width - temp) / 2;

		}else{
			// make taller
			double temp = inner.height;	
			inner.height = inner.width / outer_ratio;
			inner.y = inner.y - (inner.height - temp) / 2;
		}
	}

	/**
	 * Converts a two Points to a Rectangle. Their relative location is irrelevant.
	 * @param a The first Point.
	 * @param b The second Point.
	 * @return A rectangle with x,y in the upper left corner.
	 */
	public static Rectangle2D.Double point2DToRectangle(Point2D.Double a, Point2D.Double b){
		Rectangle2D.Double p;
		if(b.x < a.x){ //If the second point is to the left of the first point, then do this 
			if(b.y < a.y){ //If the second point is above the first point, then do this.
				p = new Rectangle2D.Double(b.x, b.y, (a.x - b.x), (a.y - b.y));
			}
			else{
				p = new Rectangle2D.Double(b.x, a.y, (a.x - b.x), (b.y - a.y));
			}
		}
		else{
			if(b. y < a.y){ //If the second point is above the first point, then do this.
				p = new Rectangle2D.Double(a.x, b.y, (b.x - a.x), (a.y - b.y));
			}
			else{//If the second point is both to the right and below the first, then do this.
					//This also happens if a.x == b.x and/or a.y == b.y.
				p = new Rectangle2D.Double(a.x, a.y, (b.x - a.x), (b.y - a.y));
			}
		}
		return p;

	}
	
	/**
	 * Creates the proper UTM rectangle for zooming with the mouse.
	 * 
	 * @param a The first point in pixels.
	 * @param b The second point in pixels.
	 * @param model The model that is shown by the view.
	 * @param view The view that has been attempted a mouse zoom on.
	 * @return The zoomed rectangle in UTM coordinates.
	 */
	public static Rectangle2D.Double mouseZoom(Point a, Point b, Rectangle2D.Double model, Rectangle view){
		Rectangle2D.Double p = point2DToRectangle(
				PointMethods.pixelToUTM(a, model, view), PointMethods.pixelToUTM(b, model, view));
		fixRatioByOuterRectangle(p, model);
		if(p.width < 200 || p.height < 200){ //Prevents user from zooming in too far and getting disoriented.
			return model;
		}
		System.out.println(p);
		return p;
	}
}