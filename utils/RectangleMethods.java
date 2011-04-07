package utils;

import java.awt.geom.Rectangle2D;

public class RectangleMethods {

	public static Rectangle2D.Double move(Rectangle2D.Double old, float length, Direction d){
		switch(d.type){
		case 1:
			//East
			Double xe = old.x + (1 * old.width * length);
			return new Rectangle2D.Double(xe, old.y, old.width, old.height);
		case 2:
			//West
			Double xw = old.x + (-1 * old.width * length);
			return new Rectangle2D.Double(xw, old.y, old.width, old.height);
		case 3:
			//North
			Double yn = old.y + (1 * old.height * length);
			return new Rectangle2D.Double(old.x, yn, old.width, old.height);
		case 4:
			//South
			Double ys = old.y + (-1 * old.height * length);
			return new Rectangle2D.Double(old.x, ys, old.width, old.height);
		}
	}

	private void fixRatioByInnerRectangle(){
		//TODO Implement
	}

	private void fixRatioByOuterRectangle(){
		//TODO Implement
	}

	private Rectangle2D.Double point2DToRectangle(){
		//TODO Implement
	}

	private Rectangle2D.Double zoomRect(){
		//TODO Implement
	}
}