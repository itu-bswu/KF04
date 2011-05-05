package core;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import loader.KrakLoader;
import utils.Evaluator;
import utils.Properties;
import utils.Stopwatch;
import graphlib.Graph;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import dataobjects.QuadTree;
import gui.Line;

/**
 * Map class
 */
public class Model {

	private static final float ROAD_SEARCH_DISTANCE = 200;
	public static final int[] part1 = new int[]{0,1,2,3,4,21,22,31,32,33,34,35,41,42,43,44,45,46,80};
	public static final int[] part2 = new int[]{5,11,23,24,25};
	public static final int[] part3 = new int[]{6,8,10,26,28,48,95,99};
	public static final int INNER_LEVEL = 2000;
	public static final int CENTER_LEVEL = 50000;

	private Rectangle2D.Double bounds;
	private Rectangle2D.Double maxBounds;
	private ArrayList<KrakEdge> path = new ArrayList<KrakEdge>();
	private List<QuadTree<KrakEdge>> qt = new ArrayList<QuadTree<KrakEdge>>();
	public Graph<KrakEdge,KrakNode> graph;

	/**
	 * Constructor
	 * Initialize variables. 
	 * Set the map to look at the entire graph.
	 */
	public Model() {
		this(null);
	}

	/**
	 * Constructor
	 * Initialize variables. 
	 * Set the map to look at the specified graph.
	 */
	public Model(Graph<KrakEdge, KrakNode> inputGraph) {

		// If the inputGraph is null, load from file.
		if (inputGraph == null) {
			graph = loadGraph();
		} else {
			graph = inputGraph;
		}
		
		maxBounds = maxBounds(graph.getNodes());
		bounds = originalBounds();
		Stopwatch sw = new Stopwatch("Quadtrees");
		createQuadTrees(graph.getAllEdges());
		sw.printTime();
	}

	/**
	 * Create Graph object from Krak data-files.
	 * @return Graph object from Krak files.
	 */
	private Graph<KrakEdge, KrakNode> loadGraph() {
		Graph<KrakEdge, KrakNode> graph = null;
		try {
			File dataDir = new File(".", Properties.get("dataDir"));
			Stopwatch sw = new Stopwatch("Graph");
			graph = KrakLoader.graphFromFiles(
					new File(dataDir, Properties.get("nodeFile"))
					.getAbsolutePath(),
					new File(dataDir, Properties.get("edgeFile"))
					.getAbsolutePath());
			sw.printTime();
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input. System will now exit.");
			System.exit(0);
		}

		return graph;
	}

	/**
	 * Create QuadTrees
	 * @param content
	 */
	private void createQuadTrees(Set<KrakEdge> content) {
		qt = new ArrayList<QuadTree<KrakEdge>>(3);

		Set<KrakEdge> set1 = new HashSet<KrakEdge>();
		Set<KrakEdge> set2 = new HashSet<KrakEdge>();
		Set<KrakEdge> set3 = new HashSet<KrakEdge>();

		boolean found;
		for(KrakEdge edge : content){
			found = false;
			// Highways and larger roads
			for(int i : part1){
				if(edge.type == i){
					set1.add(edge);
					found = true;
					break;
				}
			}

			// regular sized roads
			if(!found){
				for(int i : part2){
					if(edge.type == i){
						set2.add(edge);
						found = true;
						break;
					}
				}
			}

			// smaller roads and paths
			if(!found){
				for(int i : part3){
					if(edge.type == i){
						set3.add(edge);
						found = true;
						break;
					}
				}
			}
		}
		qt.add(new QuadTree<KrakEdge>(bounds,set1));
		qt.add(new QuadTree<KrakEdge>(bounds,set2));
		qt.add(new QuadTree<KrakEdge>(bounds,set3));
	}

	/**
	 * Create a new DijkstraSP from the startNode, and finds the path to the 
	 * endNode. The path is returned as an arraylist of lines
	 * 
	 * @param startNode The start position.
	 * @param endNode The destination.
	 * @eval An evaluator deciding whether we are going by bike, car...
	 * @throws NoPathException 
	 */
	public void findPath(KrakNode startNode, KrakNode endNode, Evaluator eval) throws NoPathException{

		if (startNode	== null) throw new NullPointerException("startNode is null");
		if (endNode		== null) throw new NullPointerException("endNode is null");

		path.addAll(Dijkstra.findPath(graph, startNode, endNode,eval));
	}
	
	/**
	 * Create a new DijkstraSP from the startNode, and finds the path to the 
	 * endNode. The path is returned as an arraylist of lines. The default 
	 * evaluator is used.
	 * 
	 * @param startNode The start position.
	 * @param endNode The destination.
	 * @throws NoPathException 
	 */
	public void findPath(KrakNode startNode, KrakNode endNode) throws NoPathException {
		this.findPath(startNode, endNode, Evaluator.DEFAULT);
	}

	/**
	 * Get the path as an ArrayList of lines
	 */
	public ArrayList<Line> getPath() {
		ArrayList<Line> lines = new ArrayList<Line>(); 
		for (KrakEdge e : path) {	
			Line line = getLine(e);
			line.setToPath();
			lines.add(line);
		}
		return lines;
	}

	/**
	 * Calculate the total distance of the current route.
	 * @return the total distance in kilometers.
	 */
	public float getRouteDistance(){
		float total = 0.0f;
		for(KrakEdge e : path){
			total += e.length;
		}
		return total/1000.0f;
	}

	/**
	 * Calculates the time needed to travel the current route (at the speed limits).
	 * @return Total (in minutes) time to travel the route.
	 */
	public float getRouteTime(){
		float minutes = 0.0f;
		for(KrakEdge e : path){
			minutes += e.DRIVETIME;
		}
		return minutes;
	}

	/**
	 * Querries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	private Set<KrakEdge> query(Rectangle2D.Double qarea){
		double area = (qarea.width/1000)*(qarea.height/1000);
		//System.out.printf("area: %.2f km2\n",area);
		Set<KrakEdge> total;

		total = qt.get(0).query(qarea);
		try {
			if(area < CENTER_LEVEL){
				total.addAll(qt.get(1).query(qarea));
				if(area < INNER_LEVEL){
					total.addAll(qt.get(2).query(qarea));
				}
			}
		} catch (Exception e) {
			// Only return what has already been found, and don't care about 
			// the rest. They will be available later.

			// Alternative solution:
			//Thread.yield();
			//return query(qarea);
		}

		return total;
	}

	/**
	 * Update the bounds
	 * @param view The rectangle of the view to zoom to.
	 */
	public void updateBounds(Rectangle2D.Double bounds) {
		if (bounds == null) throw new NullPointerException("Trying to set the bounds to null");
		if (bounds.width < 0) throw new IllegalArgumentException("The width of the rectangle is negative");
		if (bounds.height < 0) throw new IllegalArgumentException("The height of the rectangle is negative");

		this.bounds = bounds;
	}

	/**
	 * Get the bounds of the map. The bounds are what the user is currently looking at
	 * @return The bounds
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
	}

	/**
	 * Sets the Map boundaries back to the outer bounds calculated at start-up.
	 */
	public Rectangle2D.Double originalBounds(){
		return new Rectangle2D.Double(maxBounds.x, maxBounds.y, maxBounds.width, maxBounds.height);
	}

	/**
	 * Get the the bounds of the smallest possible rectangle, still showing the
	 * entire graph.
	 * 
	 * @param list
	 * @return 
	 * @return The outer bounds
	 */
	//TODO this should be deleted, the data should be saved in the inforamtion loader textfile
	private Rectangle2D.Double maxBounds(List<KrakNode> list) {
		float minX = -1;
		float minY = -1;
		float maxX = -1;
		float maxY = -1;

		for (KrakNode node : list) {

			if (node == null)
				continue;

			if ((node.getX() < minX) || (minX == -1))
				minX = (float) node.getX();
			if ((node.getX() > maxX) || (maxX == -1))
				maxX = (float) node.getX();
			if ((node.getY() < minY) || (minY == -1))
				minY = (float) node.getY();
			if ((node.getY() > maxY) || (maxY == -1))
				maxY = (float) node.getY();
		}

		return new Rectangle2D.Double(minX, minY, maxX - minX, maxY - minY);
	}

	/**
	 * Get the line of the corresponding edge
	 * @param e
	 * @return
	 */
	private Line getLine(KrakEdge e) {
		Point2D.Double firstPoint = relativePoint(new Point2D.Double(e.getStart().getX(),e.getStart().getY()));
		Point2D.Double secondPoint = relativePoint(new Point2D.Double(e.getEnd().getX(),e.getEnd().getY()));
		//Choosing the right color and thickness for each line
		Color yellow = new Color(214,207,0);
		Color roadColor = new Color(0x000000);
		int thickness = 1;
		switch(e.type){
		case 1:
			//motorvej
			roadColor = Color.RED;
			thickness = 3;
			break;
		case 2:
			//Motortrafikvej
			roadColor = Color.RED;
			thickness = 3;
			break;
		case 3:
			//Primærrute > 6 meter
			roadColor = yellow;
			thickness = 2;
			break;
		case 4:
			//Sekundærrute > 6 meter
			roadColor = yellow;
			thickness = 2;
			break;
		case 5:
			//Vej 3 - 6 meter
			roadColor = Color.ORANGE;
			break;
		case 6:
			//Anden vej
			roadColor = Color.ORANGE;
			break;
		case 8:
			//sti
			roadColor = Color.GRAY;
			break;
		case 10:
			//markvej
			roadColor = Color.ORANGE;
			break;
		case 11:
			//gågader
			roadColor = Color.GRAY;
			break;
		case 21:
			//proj. motorvej
			roadColor = Color.BLUE;
			break;
		case 22:
			//proj. motortrafikvej
			roadColor = Color.BLUE;
			break;
		case 23:
			//proj. primærvej
			roadColor = Color.BLUE;
			break;
		case 24:
			//proj. sekundærvej
			roadColor = Color.BLUE;
			break;
		case 25:
			//Proj. vej 3-6 m
			roadColor = Color.BLUE;
			break;
		case 26:
			//Proj. vej < 3 m
			roadColor = Color.BLUE;
			break;
		case 28:
			//Proj. sti
			roadColor = Color.BLUE;
			break;
		case 31:
			//Motorvejsafkørsel
			roadColor = Color.RED;
			thickness = 3;
			break;
		case 32:
			//Motortrafikvejsafkørsel
			roadColor = Color.RED;
			thickness = 3;
			break;
		case 33:
			//Primærvejsafkørsel
			roadColor = yellow;
			thickness = 2;
			break;
		case 34:
			//Sekundærvejsafkørsel
			roadColor = yellow;
			break;
		case 35:
			//Anden vejafkørsel
			roadColor = yellow;
			break;
		case 41:
			//Motorvejstunnel
			roadColor = Color.RED;
			thickness = 3;
			break;
		case 42:
			//Motortrafikvejstunnel
			roadColor = Color.RED;
			thickness = 3;
			break;

		}
		return new Line(firstPoint,secondPoint,roadColor,thickness,e.roadname);
	}

	/**
	 * Get all lines corresponding to the edges shown in the map. 
	 * @return All the lines.
	 */
	public Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		for (KrakEdge e : query(bounds)) {
			lines.add(getLine(e));
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
		float nx = (float) (	(coordinates.getX()-bounds.getX()) / bounds.getWidth()	); 
		float ny = (float) (1 - (coordinates.getY()-bounds.getY()) / bounds.getHeight()	);
		return new Point2D.Double(nx,ny);
	}

	/**
	 * Get closest edge within 200 meters.
	 * @param point the point to search from.
	 * @return the closest edge within the maximum search distance.
	 * @throws NothingCloseException If there are no edges within the maximum search distance.
	 */
	public KrakEdge getClosestEdge(Point2D.Double point, float radius) throws NothingCloseException{
		//System.out.println("Finding closest road");
		// get all nearby roads

		//System.out.println(point);

		Rectangle2D.Double search_area = new Rectangle2D.Double(point.x - radius,
				point.y - radius,
				2*radius,
				2*radius);
		Set<KrakEdge> all = query(search_area);

		// find the closest
		float distance = Integer.MAX_VALUE;
		KrakEdge closest = null;

		//System.out.println(all.size()+" roads within 200 meters");
		for(KrakEdge edge : all){
			if(edge.roadname.length() > 1){
				double cur_dist = edge.getLine().ptSegDist(point);
				//System.out.println("\t"+edge.roadname+" is "+(int)cur_dist+" meters away");
				if(cur_dist < distance){
					distance = (float) cur_dist;
					closest = edge;
				}
			}
		}

		// return the name of the edge (road)
		if(closest != null && distance < radius){
			//System.out.printf("found road: "+closest.roadname+" %.2f meters away\n",distance);
			return closest;
		}else{
			throw new NothingCloseException("no edge within a distance of "+Model.ROAD_SEARCH_DISTANCE);
		}
	}

	/**
	 * Gives the name of the closest road from a given point.
	 * @param point the point to search from
	 * @return the name of the closest road. If there is no path it will return a String of one whitespace.
	 */
	public String getClosestRoadname(Point2D.Double point){
		KrakEdge road;
		try {
			road = getClosestEdge(point,Model.ROAD_SEARCH_DISTANCE);
			return road.roadname;
		} catch (NothingCloseException e) {
			return " ";
		}
	}

	/**
	 * Finds the closest node within 200 meters from a given point
	 * @param point the given point
	 * @return the closest node from the point
	 * @throws NothingCloseException If there are no nodes within the maximum search distance.
	 */
	public KrakNode getClosestNode(Point2D.Double point) throws NothingCloseException{
		float curDistance = Model.ROAD_SEARCH_DISTANCE;
		KrakEdge edge = null;
		while(edge == null){
			try{
				edge = getClosestEdge(point, curDistance);
			}catch(NothingCloseException e){
				if(2*curDistance > maxBounds.width && 2*curDistance > maxBounds.height){
					throw e;
				}
				curDistance *= 2;
			}
		}

		KrakNode first = edge.getEnd();
		KrakNode second = edge.getOtherEnd(first);

		if(first.getPoint().distance(point) < second.getPoint().distance(point)){
			
			return first;
		}
		return second;
	}

	public void clearPath(){
		path.clear();
	}
}
