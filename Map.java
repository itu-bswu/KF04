import graphlib.Graph;
import java.awt.geom.Point2D;
import java.util.HashSet;

/**
 * Map class
 */
public class Map {
	
	private Point2D point1;
	private Point2D point2;
	private Graph graph;
	
	/**
	 * Constructor
	 */
	public Map() {
		
	}
	
	/**
	 * Zoom in or out of the graph
	 */
	public void zoom(Point2D point1, Point2D point2) {
		
	}
	
	/**
	 * Move around on the map
	 */
	public void move() {
		
	}
	
	/**
	 * Get edges from the map
	 */
	public HashSet<Edge> getEgdesGraph() {
		return null;
	}
	
	/**
	 * Get lines
	 */
	public Line[] getLines() {
		
		Line[] lines = new Line[10 /*Number of egdes in graph*/ ];
		
		return lines;
	}
	
}
