package gui;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Line class
 */
public class Line {
	private Point2D.Double startPoint;
	private Point2D.Double endPoint;
	private Color roadColor;
	private int thickness;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, int thickness) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
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
	
	@Override
	public String toString(){
		return "x1="+startPoint.x+" y1="+startPoint.y+" x2="+endPoint.x+" y2="+endPoint.y+" color="+roadColor;
	}

	public float getThickness() {
		return thickness;
	}
}
