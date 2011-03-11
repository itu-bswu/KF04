import java.awt.geom.Point2D;

/**
 * Line class
 */
public class Line {
	private Point2D.Double startPoint;
	private Point2D.Double endPoint;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double startPoint, Point2D.Double endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
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
}
