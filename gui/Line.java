package gui;
import java.awt.Color;
import java.awt.geom.Point2D;

/**
 * Line class
 */
public class Line {
	private Point2D.Float startPoint;
	private Point2D.Float endPoint;
	private Color roadColor;
	public float thickness;
	public String name;
	private int size;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Float startPoint, Point2D.Float endPoint, Color roadColor, float thickness, int size, String name) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.size = size;
		this.name = name;
	}
	
	public Line(Point2D.Float startPoint, Point2D.Float endPoint, Color roadColor, float thickness, int size) {
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
	public Point2D.Float getStartPoint() {
		return startPoint;
	}
	/**
	 * Get end point
	 */
	public Point2D.Float getEndPoint() {
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
	
	public void setThickness(float thickness){
		this.thickness = thickness;
	}
	
	public int getSize(){
		return size;
	}
}
