package utils;

import core.Model;
import gui.View;
import java.awt.Point;
import java.awt.geom.Point2D;

public class PointMethods {

	/**
	 * Converts a pixel Point to a point with UTM coordinates.
	 * 
	 * @param e The pixel-point from the screen.
	 * @param m The model containing the data to be used.
	 * @param v The view the point originated in.
	 * @return The UTM point.
	 */
	public static Point2D.Float pixelToUTM(Point e, Model model, View view){
		//Inverts the y-value of the point, so that it is converted from the pixel coordinate system to the
		//UTM coordinate system
		e.y = view.getCanvasHeight() - e.y;
		// Convert pixel to meters
		float x_m = (float) (model.getBounds().x + (e.getX() / (float) view.getCanvasWidth()) * model.getBounds().width);
		float y_m = (float) (model.getBounds().y + (e.getY() / (float) view.getCanvasHeight()) * model.getBounds().height);
		return new Point2D.Float(x_m, y_m);	
	}
 
	/**
	 * Checks if a Point object is out of bounds of the canvas and changes it to be inside the bounds. 
	 * @param outOfBounds The Point object to be checked.
	 * @param view The view that the point originated in.
	 */
	public static void pointOutOfBounds(Point outOfBounds, View view){
		if(outOfBounds.x > view.getCanvasWidth()){
			outOfBounds.x = view.getCanvasWidth();
		}
		else if(outOfBounds.x < 0){
			outOfBounds.x = 0;
		}
		if(outOfBounds.y > view.getCanvasHeight()){
			outOfBounds.y = view.getCanvasHeight();
		}
		else if(outOfBounds.y < 0){
			outOfBounds.y = 0;
		}
	}
	
	public static Point UTMToPixel(Point2D.Float e, Model model, View view){
		int x_m = (int) (((e.getX() - model.getBounds().x) / model.getBounds().width) * view.getCanvasWidth());
		int y_m = (int) (((e.getY() - model.getBounds().y) / model.getBounds().height) * view.getCanvasHeight());
		
		y_m = view.getCanvasHeight() - y_m;
		
		return new Point(x_m, y_m); 
	}
}