package core;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pathfinding.Dijkstra;
import pathfinding.NoPathException;
import pathfinding.NotPassableException;
import loader.KrakLoader;
import utils.Colors;
import utils.Evaluator;
import utils.MD5Checksum;
import utils.Properties;
import utils.Stopwatch;
import graphlib.Graph;
import dataobjects.KrakEdge;
import dataobjects.KrakNode;
import dataobjects.QuadTree;
import exceptions.NothingCloseException;
import gui.Line;

/**
 * Map class
 */
public class Model {

	// How to divide roads into quadtrees by road-type (int).
	public static final int[] part1 = new int[]{1, 2, 21, 22};
	public static final int[] part2 = new int[]{3, 4, 23, 24};
	public static final int[] part3 = new int[]{31, 32, 41, 42, 80};
	public static final int[] part4 = new int[]{33, 34, 35, 43, 44, 45};
	public static final int[] part5 = new int[]{5, 25, 46};
	public static final int[] part6 = new int[]{6, 26, 99};
	public static final int[] part7 = new int[]{0, 8, 10, 11, 28, 48, 95};
	
	public static final int[] quadTreeLimits = new int[]{Integer.MAX_VALUE,40000,10000,4000,500,125};
	private static final double ROAD_SEARCH_DISTANCE = 200;

	private Rectangle2D.Double bounds;
	private Rectangle2D.Double maxBounds;
	private ArrayList<KrakEdge> path = new ArrayList<KrakEdge>();
	private List<QuadTree<KrakEdge>> qt = Collections.synchronizedList(new ArrayList<QuadTree<KrakEdge>>());
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
		
		boolean fromFile = false;
		if (inputGraph == null) {
			try {
				File dataDir = new File(".", Properties.get("dataDir"));
				String chk = MD5Checksum.getMD5Checksum(new File(dataDir, Properties.get("nodeFile")).getAbsolutePath());
				if (chk.equals(Properties.get("nodeFileChecksum"))) {
					fromFile = true;
				}
			} catch (Exception e) {
				fromFile = false;
			}
		}

		try {
			if (!fromFile) {
				throw new Exception("Could not load serialized objects - creating datastructures");
			}

			
			Stopwatch sw = new Stopwatch("Loading");
			graph = inputGraph;
			// Load serialized objects
			if (inputGraph==null) {
				loadSerializedFromFiles();
			}
			sw.printTime();

		} catch (Exception e) {


			System.out.println(e.getMessage());

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
			
			// Save all important objects to files.
			if (inputGraph==null) {
				serializeToFiles();
			}
		}
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
			throw new IllegalStateException("Could not load graph.");
		}

		return graph;
	}
	
	/**
	 * Create QuadTrees
	 * @param content
	 */
	private void createQuadTrees(Set<KrakEdge> content) {
		qt = new ArrayList<QuadTree<KrakEdge>>();

		Set<KrakEdge> set1 = new HashSet<KrakEdge>();
		Set<KrakEdge> set2 = new HashSet<KrakEdge>();
		Set<KrakEdge> set3 = new HashSet<KrakEdge>();
		Set<KrakEdge> set4 = new HashSet<KrakEdge>();
		Set<KrakEdge> set5 = new HashSet<KrakEdge>();
		Set<KrakEdge> set6 = new HashSet<KrakEdge>();
		Set<KrakEdge> set7 = new HashSet<KrakEdge>();

		boolean found;
		for(KrakEdge edge : content){
			found = false;
			// Highways
			for(int i : part1){
				if(edge.type == i){
					set1.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;
			
			// Larger roads
			for(int i : part2){
				if(edge.type == i){
					set2.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;

			// Highway exits, tunnels and ferries
			for(int i : part3){
				if(edge.type == i){
					set3.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;

			// (Larger) road exits and tunnels
			for(int i : part4){
				if(edge.type == i){
					set4.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;
			
			// Smaller roads
			for(int i : part5){
				if(edge.type == i){
					set5.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;
			
			// Smaller roads
			for(int i : part6){
				if(edge.type == i){
					set6.add(edge);
					found = true;
					break;
				}
			}
			if (found) continue;
			
			// Paths, pedestrian zones
			for(int i : part7){
				if(edge.type == i){
					set7.add(edge);
					found = true;
					break;
				}
			}
		}
		qt.add(new QuadTree<KrakEdge>(bounds,set1));
		qt.add(new QuadTree<KrakEdge>(bounds,set2));
		qt.add(new QuadTree<KrakEdge>(bounds,set3));
		qt.add(new QuadTree<KrakEdge>(bounds,set4));
		qt.add(new QuadTree<KrakEdge>(bounds,set5));
		qt.add(new QuadTree<KrakEdge>(bounds,set6));
		qt.add(new QuadTree<KrakEdge>(bounds,set7));
	}

	/**
	 * @param serializeGraph 
	 * 
	 */
	private void serializeToFiles () {
		new Thread () {
			@Override
			public void run () {
				Stopwatch sw = new Stopwatch("Serialize");
				// Serialize
				try {
					BufferedOutputStream fout;
					ObjectOutputStream oos;

					fout = new BufferedOutputStream(new FileOutputStream(Properties.get("dataNodeEdge")));
					oos = new ObjectOutputStream(fout);

					oos.writeObject(maxBounds);
					oos.flush();

					oos.writeObject(qt.get(0));
					oos.flush();
					
					oos.writeObject(qt.get(1));
					oos.flush();

					oos.writeObject(graph);
					oos.flush();

					// Skip first two quadtrees (has already been serialized).
					for (int i = 2; i < qt.size(); i++) {
						oos.writeObject(qt.get(i));
						oos.flush();
					}
						
					oos.close();

					File dataDir = new File(".", Properties.get("dataDir"));
					String chk = MD5Checksum.getMD5Checksum(new File(dataDir, Properties.get("nodeFile")).getAbsolutePath());
					Properties.set("nodeFileChecksum", chk);
					Properties.save();
				} catch (Exception ex) {
					System.out.println("Serialization failed.");
					//ex.printStackTrace();
				}
				sw.printTime();
			}
		}.start();
	}

	private void loadSerializedFromFiles () throws IOException, ClassNotFoundException {
		BufferedInputStream bin;
		bin = new BufferedInputStream(new FileInputStream(Properties.get("dataNodeEdge")));
		final ObjectInputStream ois = new ObjectInputStream(bin);

		maxBounds = (Rectangle2D.Double) ois.readObject();
		bounds = originalBounds();

		qt.add((QuadTree<KrakEdge>) ois.readObject());
		qt.add((QuadTree<KrakEdge>) ois.readObject());

		new Thread() {
			@Override
			public void run () {
				Stopwatch sw = new Stopwatch("Load serialized");
				try {
					graph = (Graph<KrakEdge, KrakNode>) ois.readObject();

					// Quadtrees
					for (int i = 0; i < quadTreeLimits.length-1; i++) {
						qt.add((QuadTree<KrakEdge>) ois.readObject());
					}

					ois.close();

				} catch (Exception e) {
					System.out.println(e.getMessage());
					throw new RuntimeException("Failed to load all of the neccesary data.");
				}
				sw.printTime();
			}
		}.start();
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
			line.setRoadColor(Colors.ROUTE);
			lines.add(line);
		}
		return lines;
	}

	/**
	 * Calculate the total distance of the current route.
	 * @return the total distance in kilometers.
	 */
	public double getRouteDistance(){
		double total = 0.0f;
		for(KrakEdge e : path){
			total += e.length;
		}
		return total/1000.0;
	}

	/**
	 * Calculates the time needed to travel the current route (at the speed limits).
	 * @return Total (in minutes) time to travel the route.
	 */
	public double getRouteTime(){
		double minutes = 0.0;
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
	private List<KrakEdge> query(Rectangle2D.Double qarea) {
		return query(qarea,false);
	}
	/**
	 * Queries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	private List<KrakEdge> query(Rectangle2D.Double qarea,Boolean getAll){
		double area = (qarea.width/1000)*(qarea.height/1000);
		//System.out.printf("area: %.2f km2\n",area);
		List<KrakEdge> total = new ArrayList<KrakEdge>();
		try {
			for(int index = qt.size()-1; index > 0; index--){
				if(area < quadTreeLimits[index-1]||(getAll)){
					total.addAll(qt.get(index).query(qarea));
					
				}
			}
			total.addAll(qt.get(0).query(qarea));

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
	 * Get the bounds of the map. The bounds are what the user is currently looking at
	 * @return The bounds
	 */
	public Rectangle2D.Double getBounds() {
		return bounds;
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
	 * Sets the Map boundaries back to the outer bounds calculated at start-up.
	 */
	public Rectangle2D.Double originalBounds(){
		
		if (maxBounds==null) {
			maxBounds = maxBounds(graph.getNodes());
		}
		
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
	private Rectangle2D.Double maxBounds(List<KrakNode> list) {
		double minX = -1;
		double minY = -1;
		double maxX = -1;
		double maxY = -1;

		for (KrakNode node : list) {

			if (node == null)
				continue;

			if ((node.getX() < minX) || (minX == -1))
				minX = node.getX();
			if ((node.getX() > maxX) || (maxX == -1))
				maxX = node.getX();
			if ((node.getY() < minY) || (minY == -1))
				minY = node.getY();
			if ((node.getY() > maxY) || (maxY == -1))
				maxY = node.getY();
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
		Color roadColor = Colors.SMALL_ROAD;
		int size = 1;
		switch(e.type){
		case 1:
			//motorvej
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 2:
			//Motortrafikvej
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 3:
			//Primærrute > 6 meter
			roadColor = Colors.LARGE_ROAD;
			size = 2;
			break;
		case 4:
			//Sekundærrute > 6 meter
			roadColor = Colors.LARGE_ROAD;
			size = 2;
			break;
		case 5:
			//Vej 3 - 6 meter
			roadColor = Colors.SMALL_ROAD;
			break;
		case 6:
			//Anden vej
			roadColor = Colors.SMALL_ROAD;
			break;
		case 8:
			//sti
			roadColor = Colors.PATH;
			break;
		case 10:
			//markvej
			roadColor = Colors.SMALL_ROAD;
			break;
		case 11:
			//gågader
			roadColor = Colors.PATH;
			break;
		case 21:
			//proj. motorvej
			roadColor = Colors.HIGHWAY;
			break;
		case 22:
			//proj. motortrafikvej
			roadColor = Colors.HIGHWAY;
			break;
		case 23:
			//proj. primærvej
			roadColor = Colors.LARGE_ROAD;
			break;
		case 24:
			//proj. sekundærvej
			roadColor = Colors.LARGE_ROAD;
			break;
		case 25:
			//Proj. vej 3-6 m
			roadColor = Colors.SMALL_ROAD;
			break;
		case 26:
			//Proj. vej < 3 m
			roadColor = Colors.SMALL_ROAD;
			break;
		case 28:
			//Proj. sti
			roadColor = Colors.PATH;
			break;
		case 31:
			//Motorvejsafkørsel
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 32:
			//Motortrafikvejsafkørsel
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 33:
			//Primærvejsafkørsel
			roadColor = Colors.LARGE_ROAD;
			size = 2;
			break;
		case 34:
			//Sekundærvejsafkørsel
			roadColor = Colors.LARGE_ROAD;
			break;
		case 35:
			//Anden vejafkørsel
			roadColor = Colors.LARGE_ROAD;
			break;
		case 41:
			//Motorvejstunnel
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 42:
			//Motortrafikvejstunnel
			roadColor = Colors.HIGHWAY;
			size = 3;
			break;
		case 80:
			// færge
			roadColor = Colors.OCEAN;
			size = 1;
			break;
		}
		
		double thickness = 8.0/bounds.width;
		
		return new Line(firstPoint,secondPoint,roadColor,thickness,size,e.roadname);
	}

	/**
	 * Get all lines corresponding to the edges shown in the map. 
	 * @return All the lines.
	 */
	public Collection<Line> getLines() {
		ArrayList<Line> lines = new ArrayList<Line>();
		for (KrakEdge e : query(bounds)) {
			lines.add(getLine(e));
		}
		//System.out.println("Drawing " + lines.size() + " lines.");
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
		double ny = 1 - (coordinates.getY()-bounds.getY()) / bounds.getHeight();
		return new Point2D.Double(nx,ny);
	}

	/**
	 * Get closest edge within within a specified number of meters.
	 * @param point the point to search from.
	 * @param an evaluator to eliminate roads that can't be traveled by the given travel mode
	 * @return the closest edge within the maximum search distance.
	 * @throws NothingCloseException If there are no edges within the maximum search distance.
	 */
	public KrakEdge getClosestEdge(Point2D.Double point, double radius, Evaluator eval) throws NothingCloseException{
		//System.out.println("Finding closest road");
		// get all nearby roads

		//System.out.println(point);

		Rectangle2D.Double search_area = new Rectangle2D.Double(point.x - radius,
				point.y - radius,
				2*radius,
				2*radius);
		List<KrakEdge> all = query(search_area);

		// find the closest
		double distance = Integer.MAX_VALUE;
		KrakEdge closest = null;

		//System.out.println(all.size()+" roads within 200 meters");
		for(KrakEdge edge : all){
			if(edge.roadname.length() > 1){
				double cur_dist = edge.getLine().ptSegDist(point);
				//System.out.println("\t"+edge.roadname+" is "+(int)cur_dist+" meters away");
				try{
					eval.evaluate(edge); // we don't need the return, just the exception if it is unpassable
					if(cur_dist < distance){
						distance = cur_dist;
						closest = edge;
					}
				}catch(NotPassableException e){} // silent catch to avoid having the unpassable edge as closest
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
			road = getClosestEdge(point,Model.ROAD_SEARCH_DISTANCE, Evaluator.ANYTHING);
			return road.roadname;
		} catch (NothingCloseException e) {
			return " ";
		}
	}

	/**
	 * Finds the closest node in meters from a given point
	 * @param point the given point
	 * @param an evaluator to eliminate roads that can't be traveled by the given travel mode
	 * @return the closest node from the point
	 * @throws NothingCloseException If there are no nodes within the maximum search distance.
	 */
	public KrakNode getClosestNode(Point2D.Double point, Evaluator eval) throws NothingCloseException{
		double curDistance = Model.ROAD_SEARCH_DISTANCE;
		KrakEdge edge = null;
		while(edge == null){
			try{
				edge = getClosestEdge(point, curDistance, eval);
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

	/**
	 * Removes the entire saved route.
	 */
	public void clearPath(){
		path.clear();
	}
}
