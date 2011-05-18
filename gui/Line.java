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
	public double thickness;
	public String name;
	private int size;
	
	/**
	 * Creates a Line object with exactly the information that is given here.
	 * @param firstPoint One end of the line.
	 * @param secondPoint The other end of the line.
	 * @param roadColor The color the line should be drawn in.
	 * @param thickness The width of the line.
	 * @param size A factor for scaling the size (thickness) of the line.
	 * @param name If the line needs a name.
	 */
	public Line(Point2D.Double firstPoint, Point2D.Double secondPoint, Color roadColor, double thickness, int size, String name) {
		this.startPoint = firstPoint;
		this.endPoint = secondPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.size = size;
		this.name = name;
	}
	
	/**
	 * Creates a Line object with exactly the information that is given here.
	 * @param firstPoint One end of the line.
	 * @param secondPoint The other end of the line.
	 * @param roadColor The color the line should be drawn in.
	 * @param thickness The width of the line.
	 * @param size A factor for scaling the size (thickness) of the line.
	 */
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
	 * Get the color of the Line
	 * @return The color of the Line.
	 */
	public Color getLineColor() {
		return roadColor;
	}
	
	/**
	 * Set the color of the Line.
	 * @param c The new color of the Line.
	 */
	public void setLineColor(Color c){
		roadColor = c;
	}
	
	@Override
	public String toString(){
		return "x1="+startPoint.x+" y1="+startPoint.y+" x2="+endPoint.x+" y2="+endPoint.y+" color="+roadColor;
	}

	/**
	 * Get the thickness of the Line.
	 * @return The thickness of the Line.
	 */
	public double getThickness() {
		return thickness;
	}
	
	/**
	 * Set the thickness of the Line.
	 * @param thickness Set the thickness of the Line.
	 */
	public void setThickness(double thickness){
		this.thickness = thickness;
	}
	
	/**
	 * Get the size of the Line.
	 * @return The size of the Line.
	 */
	public int getSize(){
		return size;
	}
}
