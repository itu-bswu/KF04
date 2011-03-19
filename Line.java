import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Line class
 */
public class Line {
	private Point2D.Double startPoint;
	private Point2D.Double endPoint;
	private Color roadColor;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
	}
	
	/**
	 * Get start point
	 */
	public Point2D.Double getStartPoint() {
		return startPoint;
	}
	/**
	 * Get end point
	 */
	public Point2D.Double getEndPoint() {
		return endPoint;
	}

	/**
	 * @return the roadColor
	 */
	public Color getRoadColor() {
		return roadColor;
	}
}
