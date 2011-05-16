package utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PointMethods {

	/**
	 * Converts a pixel Point to a point with UTM coordinates.
	 * 
	 * @param e The pixel-point from the screen.
	 * @param m The model containing the data to be used.
	 * @param v The view the point originated in.
	 * @return The UTM point.
	 */
	public static Point2D.Double pixelToUTM(Point e, Rectangle2D.Double model, Rectangle view){
		//Inverts the y-value of the point, so that it is converted from the pixel coordinate system to the
		//UTM coordinate system
		e.y = view.height - e.y;
		// Convert pixel to meters
		double x_m = model.x + (e.getX() / view.width) * model.width;
		double y_m = model.y + (e.getY() / view.height) * model.height;
		return new Point2D.Double(x_m, y_m);	
	}
	
	/**
	 * Used to convert UTM points to pixel values.
	 * 
	 * @param e The UTM-point to be converted.
	 * @param model The model the point same from.
	 * @param view The view the point originated in.
	 * @return The pixel point.
	 */
	public static Point UTMToPixel(Point2D.Double e, Rectangle2D.Double model, Rectangle view){
		int x_m = (int) (((e.getX() - model.x) / model.width) * view.width);
		int y_m = (int) (((e.getY() - model.y) / model.height) * view.height);
		
		y_m = view.height - y_m;
		
		return new Point(x_m, y_m); 
	}
	 
	/**
	 * Checks if a Point object is out of bounds of the canvas and changes it to be inside the bounds. 
	 * @param outOfBounds The Point object to be checked.
	 * @param view The view that the point originated in.
	 */
	public static void pointOutOfBounds(Point outOfBounds, Rectangle view){
		if(outOfBounds.x > view.width){
			outOfBounds.x = view.width;
		}
		else if(outOfBounds.x < 0){
			outOfBounds.x = 0;
		}
		if(outOfBounds.y > view.height){
			outOfBounds.y = view.height;
		}
		else if(outOfBounds.y < 0){
			outOfBounds.y = 0;
		}
	}
}