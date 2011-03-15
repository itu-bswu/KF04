import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import graphlib.Graph;

/**
 * Map class
 */
public class Map {
	
	private Rectangle2D bounds;
	private Graph<KrakEdge,KrakNode> graph; 
	private HashSet<KrakEdge> edges;
	
	/**
	 * Constructor
	 * @throws IOException 
	 */
	public Map(Graph<KrakEdge,KrakNode> graph) {
		System.out.println("Map object created");
		this.graph = graph;
		this.edges = new HashSet<KrakEdge>();
		bounds = outerBounds();
		updateEdges();
	}
	
	/**
	 * Zoom in or out of the graph
	 */
	public void zoom(Rectangle2D view) {
		this.bounds = outerBounds(); 
		updateEdges();
	}
	
	/**
	 * Update edges
	 */
	private void updateEdges() {
		//Iterator over all edge		
		for(KrakEdge edge : graph.outGoingEdges()) {
			
			System.out.println(edge);
			
			if (isInside(edge)) {
				edges.add(edge);
			}else{
				edges.remove(edge);
			}
		}
	}
	
	
	/**
	 * is inside
	 * 
	 * This function is one of the main problems of the project.
	 * The following is a simple method to check whether or not the end or start node of an edge is inside the viewpoint.
	 * But this does not solve the actual problem.  
	 */
	public Boolean isInside(KrakEdge e) {
		return (isInside(e.getStart())||isInside(e.getEnd()));
	}
	
	public Boolean isInside(KrakNode n) {
		return bounds.contains(new Point2D.Double(n.getX(),n.getY()));
	}
	
	/**
	 * Get bounds
	 */
	public Rectangle2D getBounds() {
		return bounds;
	}
	
	/**
	 * Get bounds
	 */
	private Rectangle2D outerBounds() {
		System.out.println("establishing outer bounds of map");
		
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
	 * Move around on the map
	 */
	public void move(Direction d,double length) {
		
		double horizontalChange	= d.coordinatepoint().getX() * bounds.getWidth() * length;
		double verticalChange	= d.coordinatepoint().getY() * bounds.getHeight() * length;
				
		bounds.setRect(bounds.getX()+horizontalChange, bounds.getY()+verticalChange, bounds.getWidth(), bounds.getHeight());
		
		updateEdges();
	}
	
	/**
	 * Get edges from the map
	 */
	public HashSet<KrakEdge> getEgdes() {
		return edges;
	}
	
	/**
	 * Get lines
	 */
	public Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		
		for (KrakEdge e : edges) {
			Point2D.Double firstPoint = relativePoint(e.getStart().getX(),e.getStart().getY());
			Point2D.Double secondPoint = relativePoint(e.getEnd().getX(),e.getEnd().getY());
			lines.add(new Line(firstPoint,secondPoint));
		}
		
		return lines;
	}
	
	/**
	 * Relative Point
	 * Takes two coordinates and returnes a point relative to the screen
	 */
	private Point2D.Double relativePoint(double x,double y) {
		
		double nx = (x-bounds.getX()) / bounds.getWidth(); 
		double ny = (y-bounds.getY()) / bounds.getHeight();
		
		return new Point2D.Double(nx,ny);
	}
	
}
