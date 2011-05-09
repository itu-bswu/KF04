package gui;

import java.awt.Color;
import java.awt.geom.Point2D;

import dataobjects.KrakEdge;

/**
 * Line class
 */
public class Line {
	private Point2D.Double startPoint;
	private Point2D.Double endPoint;
	private Color roadColor;
	public float thickness;
	public String name;
	private int size;
	private KrakEdge edge;
	
	/**
	 * Constructor
	 */
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, float thickness, int size, String name, KrakEdge edge) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.roadColor = roadColor;
		this.thickness = thickness;
		this.size = size;
		this.name = name;
		this.edge = edge;
	}
	
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, float thickness, int size, KrakEdge edge) {
		this(startPoint, endPoint, roadColor, thickness, size, "", edge);
	}
	
	public Line(Point2D.Double startPoint, Point2D.Double endPoint, Color roadColor, float thickness, int size) {
		this(startPoint, endPoint, roadColor, thickness, size, "", null);
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
	
	public void setThickness(float thickness){
		this.thickness = thickness;
	}
	
	public int getSize(){
		return size;
	}
	
	public KrakEdge getEdge() {
		return edge;
	}
}
