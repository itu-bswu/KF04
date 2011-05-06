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
	public int thickness;
	public String name;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, int thickness, String name) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.name = name;
	}
	
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, int thickness) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.name = "";
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
	
	/**
	 * Set the color of the road.
	 * @param c the new color.
	 */
	public void setRoadColor(Color c){
		roadColor = c;
	}
	
	@Override
	public String toString(){
		return "x1="+startPoint.x+" y1="+startPoint.y+" x2="+endPoint.x+" y2="+endPoint.y+" color="+roadColor;
	}

	public float getThickness() {
		return thickness;
	}
	
	public void setThickness(int thickness){
		this.thickness = thickness;
	}
}
