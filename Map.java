import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import graphlib.Graph;

/**
 * Map class
 */
public class Map {
	
	private Point2D startPoint = new Point2D.Double(0,100000);  	//In meters
	private Point2D endPoint = new Point2D.Double(0,100000);		//In meters
	private Graph<KrakEdge,KrakNode> graph; 
	private HashSet<KrakEdge> edges;
	
	/**
	 * Constructor
	 * @throws IOException 
	 */
	public Map(Graph<KrakEdge,KrakNode> graph) {
		this.graph = graph;
		updateEdges();
	}
	
	/**
	 * Zoom in or out of the graph
	 */
	public void zoom(Point2D point1, Point2D point2) {
		startPoint = point1;
		endPoint = point2;
		updateEdges();
	}
	
	/**
	 * Update edges
	 */
	private void updateEdges() {
		//Iterator over all edge
		
		System.out.print("?");
		System.out.println(graph.outGoingEdges());
		
		for(KrakEdge edge : graph.outGoingEdges()) {			
			if (isInside(edge)) {
				edges.add(edge);
			}else{
				edges.remove(edge);
			}
		}
	}
	
	/**
	 * Find boundries
	 */
	
	
	
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
		if(n.getX() < startPoint.getX()) return false;
		if(n.getX() > endPoint.getX()) return false;
		if(n.getY() < startPoint.getY()) return false;
		if(n.getY() > endPoint.getY()) return false;
		return true;
	}
	
	/**
	 * Move around on the map
	 * 
	 * Maybe not as clean as it could be.
	 * 
	 * I need the DIrectionclass!!!
	 * 
	 */
	public void move(Direction d,double length) {
		
		double horizontalChange	= d.coordinatepoint().getX() * Math.abs(startPoint.getX()-endPoint.getX() * length);
		double verticalChange	= d.coordinatepoint().getY() * Math.abs(startPoint.getY()-endPoint.getY()) * length;
		
		startPoint.setLocation(startPoint.getX()+horizontalChange,startPoint.getY()+verticalChange);
		endPoint.setLocation(endPoint.getX()+horizontalChange,endPoint.getY()+verticalChange);

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
	 * Why convert to array? couldn't the draw.
	 */
	public Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		
		for (KrakEdge e : edges) {
			Point2D.Double firstPoint = new Point2D.Double(e.getStart().getX(),e.getStart().getY());
			Point2D.Double secondPoint = new Point2D.Double(e.getEnd().getX(),e.getEnd().getY());
			lines.add(new Line(firstPoint,secondPoint));
		}
		
		return lines;
	}
	
}
