import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import graphlib.Graph;

/**
 * Map class
 */
public class Map {
	
	private Rectangle2D.Double bounds;
	private Graph<KrakEdge,KrakNode> graph; 
	private QuadTree<KrakEdge> qt;
	
	/**
	 * Constructor
	 * Initialize variables. 
	 * Set the map to look at the entire graph.
	 */
	public Map(Graph<KrakEdge,KrakNode> graph) {
		System.out.println("Map object created");
		this.graph = graph;
		System.out.println(graph);
		bounds = outerBounds();
		this.qt = new QuadTree<KrakEdge>(bounds,graph.getAllEdges());
		qt.query(bounds);
	}
	
	/**
	 * Zoom in or out of the graph
	 * @param view The rectangle of the view to zoom to.
	 */
	public void updateBounds(Rectangle2D.Double bounds) {
		this.bounds = bounds;
		qt.query(bounds);
	}
	
	/**
	 * Get the bounds of the map. The bounds are what the user is currently looking at
	 * @return The bounds
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
	}
	
	/**
	 * Get the the bounds of the smallest possible rectangle, still showing the entire graph.
	 * @return The outer bounds
	 */
	private Rectangle2D.Double outerBounds() {
		System.out.println("Establishing outer bounds of map");
		
		double minX = -1;
		double minY = -1;
		double maxX = -1;
		double maxY = -1;

		for(KrakNode node : graph.getNodes()) {
			
			if (node == null) continue;
			
			if ((node.getX() < minX)||(minX == -1)) minX = node.getX();
			if ((node.getX() > maxX)||(maxX == -1)) maxX = node.getX();
			if ((node.getY() < minY)||(minY == -1)) minY = node.getY();
			if ((node.getY() > maxY)||(maxY == -1)) maxY = node.getY();
		}
		
		return new Rectangle2D.Double(minX,minY,maxX-minX,maxY-minY);
	}
	
	/**
	 * Get all lines corresponding to the edges shown in the map. 
	 * @return All the lines.
	 */
	public Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		for (KrakEdge e : qt.query(bounds)) {
			Point2D.Double firstPoint = relativePoint(new Point2D.Double(e.getStart().getX(),e.getStart().getY()));
			Point2D.Double secondPoint = relativePoint(new Point2D.Double(e.getEnd().getX(),e.getEnd().getY()));
			lines.add(new Line(firstPoint,secondPoint));
		}
		return lines;
	}
	
	/**
	 * Relative Point
	 * Takes two coordinates and returns a point relative to the screen
	 * @param coordinates A point of coordinates on the map.
	 * @return The point on the screen corresponding to the coordinates given.
	 */
	private Point2D.Double relativePoint(Point2D coordinates) {
		
		double nx = (coordinates.getX()-bounds.getX()) / bounds.getWidth(); 
		double ny = (coordinates.getY()-bounds.getY()) / bounds.getHeight();
		
		return new Point2D.Double(nx,ny);
	}
	
}
