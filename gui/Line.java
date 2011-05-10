package gui;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Line class
 */
public class Line {
	private Point2D.Double startPoint;
	private Point2D.Double endPoint;
	private Color roadColor;
	public double thickness;
	public String name;
	private int size;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double firstPoint, Point2D.Double secondPoint, Color roadColor, double thickness2, int size, String name) {
		this.startPoint = firstPoint;
		this.endPoint = secondPoint;
		this.roadColor = roadColor;
		this.thickness = thickness2;
		this.size = size;
		this.name = name;
	}
	
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, double thickness, int size) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.size = size;
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

	public double getThickness() {
		return thickness;
	}
	
	public void setThickness(double thickness){
		this.thickness = thickness;
	}
	
	public int getSize(){
		return size;
	}
}
