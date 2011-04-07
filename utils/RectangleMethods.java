package utils;

import gui.View;
import core.Model;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class RectangleMethods {

	/**
	 * Creates a rectangle moved in a direction compared to the old rectangle.
	 * 
	 * @param old The rectangle to be moved.
	 * @param length How far the rectangle should be moved.
	 * @param direction Which way should the rectangle move
	 * @return The rectangle that has been moved. 
	 */
	public static Rectangle2D.Double move(Rectangle2D.Double old, float length, Direction direction){
		switch(direction){
		case WEST:
			Double xw = old.x + (-1 * old.width * length);
			return new Rectangle2D.Double(xw, old.y, old.width, old.height);
		case EAST:
			Double xe = old.x + (1 * old.width * length);
			return new Rectangle2D.Double(xe, old.y, old.width, old.height);
		case NORTH:
			Double yn = old.y + (1 * old.height * length);
			return new Rectangle2D.Double(old.x, yn, old.width, old.height);
		case SOUTH:
			Double ys = old.y + (-1 * old.height * length);
			return new Rectangle2D.Double(old.x, ys, old.width, old.height);
		}
		return old;
	}

	/**
	 * Adjusts a Rectangle to have the same ratio as another Rectangle by removing excess space (Inner Rectangle).
	 * @param a The Rectangle to adjust.
	 * @param b The Rectangle that has the desired ratio.
	 */
	public static void fixRatioByInnerRectangle(Rectangle2D.Double inner, Rectangle2D.Double outer){
		float outer_ratio = (float) (outer.width / outer.height);
		float inner_ratio = (float) (inner.width / inner.height);

		if(inner_ratio < outer_ratio){
			// cut height

			float temp = (float) inner.height;	
			inner.height = inner.width / outer_ratio;
			inner.y = inner.y - (inner.height - temp) / 2;
		}else{
			// cut width

			float temp = (float) inner.width;
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
		float outer_ratio = (float) (outer.width / outer.height);
		float inner_ratio = (float) (inner.width / inner.height);

		if(inner_ratio < outer_ratio){
			// make wider
			float temp = (float) inner.width;
			inner.width = outer_ratio * inner.height;
			inner.x = inner.x - (inner.width - temp) / 2;

		}else{
			// make taller
			float temp = (float) inner.height;	
			inner.height = inner.width / outer_ratio;
			inner.y = inner.y - (inner.height - temp) / 2;
		}
	}

	/**
	 * Converts a two Points to a Rectangle. Their relative location is irrelevant.
	 * @param a The first Point.
	 * @param b The second Point.
	 * @return A Rectangle with x,y in the upper left corner.
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
				p = new Rectangle2D.Double(a.x, a.y, (b.x - a.x), (b.y - a.y));
			}
		}
		return p;

	}

	/**
	 * Creates a Rectangle that is a zoomed in/out version of another.
	 * 
	 * @param factor The factor to zoom with, for example 0.2 for a 20% zoom.
	 * @param zoom True if zooming IN, else false.
	 * @param old The original view.
	 * @return The finished Rectangle
	 */
	public static Rectangle2D.Double zoomRect(float factor, boolean zoom, Rectangle2D.Double old){
		if(zoom){
			return new Rectangle2D.Double(old.x + factor * old.width, //x is increased by the factor in proportion to the width
					old.y + factor * old.height, //y is increased by the factor in proportion to the height
					old.width - factor * old.width * 2, //width is decreased by the factor
					old.height - factor * old.height * 2); //height is decreased by the factor
		}
		else{
			return new Rectangle2D.Double(old.x - old.width * factor, //x is decreased by the factor in proportion to the width
					old.y - old.height * factor, //y is decreased by the factor in proportion to the height
					old.width + old.width * factor * 2, //width is increased by the factor
					old.height + old.height * factor * 2);//height is increased by the factor
		}
	}

	/**
	 * Creates the proper UTM rectangle for zooming with the mouse.
	 * 
	 * @param a The first point.
	 * @param b The second point.
	 * @param model The model that is shown by the view.
	 * @param view The view that has been attempted a mouse zoom on.
	 * @return
	 */
	public static Rectangle2D.Double mouseZoom(Point a, Point b, Model model, View view){
		Rectangle2D.Double p = point2DToRectangle(
				PointMethods.pixelToUTM(a, model, view), PointMethods.pixelToUTM(b, model, view));
		fixRatioByOuterRectangle(p, model.getBounds());
		return p;
	}
}