
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Map class
 */
public class Map {
	
	private Point2D startPoint;  	//In meters
	private Point2D endPoint;		//In meters
	private graphlib.Graph<KrakEdge,KrakNode> graph; 
	private HashSet<KrakEdge> edges;
	
	/**
	 * Constructor
	 */
	public Map() {
		
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
		//Iterator over all edges:
		for(KrakNode node : graph.nodes) {
			Iterator<KrakEdge> iterator = graph.outGoingEdges(node);
			while(iterator.hasNext()) {
				KrakEdge e = iterator.next();
				if (isInside(e)) {
					edges.add(e);
				}else{
					edges.remove(e);
				}
			}
		}
	}
	
	
	/**
	 * isInside
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
		
		double horizontalChange = 0;
		double verticalChange = 0;
		
		switch (1) { //d ?????
			case (1):	
				horizontalChange = 1;
			break;
			case (2):
				verticalChange = -1;
			break;
			case (3):
				horizontalChange = -1;
			break;
			case(4):
				verticalChange = 1;
			break;
		}
		
		horizontalChange	*= Math.abs(startPoint.getX()-endPoint.getX()) * length;
		verticalChange		*= Math.abs(startPoint.getY()-endPoint.getY()) * length;
		
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
	 */
	public Line[] getLines() {
		Line[] lines = new Line[edges.size()];
		
		int i = 0;
		for (KrakEdge e : edges) {
			Point2D.Double firstPoint = new Point2D.Double(e.getStart().getX(),e.getStart().getY());
			Point2D.Double secondPoint = new Point2D.Double(e.getEnd().getX(),e.getEnd().getY());
			lines[i++] = new Line(firstPoint,secondPoint);
		}
		
		return lines;
	}
	
}
