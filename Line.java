import java.awt.geom.Point2D;

/**
 * Line class
 */
public class Line {
	private Point2D startPoint;
	private Point2D endPoint;
	
	/**
	 * Constructor
	 */
	public Line(Point2D startPoint, Point2D endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	/**
	 * Get start point
	 */
	public Point2D getStartPoint() {
		return startPoint;
	}
	/**
	 * Get end point
	 */
	public Point2D getEndPoint() {
		return endPoint;
	}
}
