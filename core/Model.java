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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import loader.KrakLoader;

import utils.MD5Checksum;
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
	public static final int[] part1 = new int[]{0,1,2,3,4,10,21,22,31,32,33,34,35,41,42,43,44,45,46,80};
	public static final int[] part2 = new int[]{5,11,23,24,25};
	public static final int[] part3 = new int[]{6,8,26,28,48,95,99};
	public static final int INNER_LEVEL = 2000;
	public static final int CENTER_LEVEL = 50000;

	private Rectangle2D.Double bounds;
	private Rectangle2D.Double maxBounds;
	private ArrayList<QuadTree<KrakEdge>> qt;
	
	public Graph<KrakEdge,KrakNode> graph;

	/**
	 * Constructor
	 * Initialize variables. 
	 * Set the map to look at the entire graph.
	 */
	@SuppressWarnings("unchecked")
	public Model() {
		boolean fromFile = false;
		try {
			File dataDir = new File(".", Properties.get("dataDir"));
			String chk = MD5Checksum.getMD5Checksum(new File(dataDir, Properties.get("nodeFile")).getAbsolutePath());
			if (chk.equals(Properties.get("nodeFileChecksum"))) {
				fromFile = true;
			}
		} catch (Exception e) {
			fromFile = false;
		}

		try {
			if (!fromFile) {
				throw new Exception("Create datastructure");
			}

			Stopwatch sw = new Stopwatch("Loading");
			BufferedInputStream bin;
			
			bin = new BufferedInputStream(new FileInputStream(Properties.get("graphFile")));
			ObjectInputStream ois = new ObjectInputStream(bin);
			graph = (Graph<KrakEdge, KrakNode>) ois.readObject();
			ois.close();
			
			bin = new BufferedInputStream(new FileInputStream(Properties.get("maxBoundsFile")));
			ois = new ObjectInputStream(bin);
			maxBounds = (Rectangle2D.Double) ois.readObject();
			bounds = originalBounds();
			ois.close();

			bin = new BufferedInputStream(new FileInputStream(Properties.get("quadTreeFile")));
			ois = new ObjectInputStream(bin);
			qt = (ArrayList<QuadTree<KrakEdge>>) ois.readObject();
			ois.close();
			
			sw.printTime();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
			graph = loadGraph();
			maxBounds = maxBounds(graph.getNodes());
			bounds = originalBounds();
			Stopwatch sw = new Stopwatch("Quadtrees");
			createQuadTrees(graph.getAllEdges());
			sw.printTime();
			
			sw = new Stopwatch("Serialize");
			// Serialize
			try {
				BufferedOutputStream fout;
				fout = new BufferedOutputStream(new FileOutputStream(Properties.get("graphFile")));
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(graph);
				oos.flush();
				oos.close();
				
				fout = new BufferedOutputStream(new FileOutputStream(Properties.get("maxBoundsFile")));
				oos = new ObjectOutputStream(fout);
				oos.writeObject(maxBounds);
				oos.flush();
				oos.close();
				
				// XXX: Serialize quadtrees individually?
				fout = new BufferedOutputStream(new FileOutputStream(Properties.get("quadTreeFile")));
				oos = new ObjectOutputStream(fout);
				oos.writeObject(qt);
				oos.flush();
				oos.close();
				
				File dataDir = new File(".", Properties.get("dataDir"));
				String chk = MD5Checksum.getMD5Checksum(new File(dataDir, Properties.get("nodeFile")).getAbsolutePath());
				Properties.set("nodeFileChecksum", chk);
				Properties.save();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			sw.printTime();
			
		}
	}
	
	/**
	 * Create Graph object from Krak data-files.
	 * 
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
			System.out
					.println("A problem occured when trying to read input. System will now exit.");
			System.exit(0);
		}

		return graph;
	}

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
	 * Create a new DijkstraSP from the startNode, and finds the path to the endNode. The path is returned as an arraylist of lines
	 */
	public ArrayList<Line> shortestPath(KrakNode startNode, KrakNode endNode) {
		ArrayList<Line>path = new ArrayList<Line>();
		DijkstraSP shortestPathTree = new DijkstraSP(graph, startNode);
		
		for (KrakEdge e : shortestPathTree.pathTo(endNode)) {
			path.add(getLine(e));
		}

		return path;
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
		if(area < CENTER_LEVEL){
			total.addAll(qt.get(1).query(qarea));
			if(area < INNER_LEVEL){
				total.addAll(qt.get(2).query(qarea));
			}
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
	 * Get the Width of the bounds
	 * @return The width of the bounds
	 */
	//TODO Denne og height metoden skal slettes og det skal coordineres med Jacob
	public float getBoundsWidth() {
		return (float) bounds.width;
	}

	/**
	 * Get the height of the bounds
	 * @return The height of the bounds
	 */
	public float getBoundsHeight() {
		return (float) bounds.height;
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
			roadColor = Color.YELLOW;
			thickness = 2;
			break;
		case 4:
			//Sekundærrute > 6 meter
			roadColor = Color.YELLOW;
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
			roadColor = Color.YELLOW;
			thickness = 2;
			break;
		case 34:
			//Sekundærvejsafkørsel
			roadColor = Color.YELLOW;
			break;
		case 35:
			//Anden vejafkørsel
			roadColor = Color.YELLOW;
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
		return new Line(firstPoint,secondPoint,roadColor,thickness);
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
	 * Get closest road
	 * @param point
	 * @return
	 */
	public String getClosestRoad(Point2D.Double point){
		//System.out.println("Finding closest road");
		// get all nearby roads

		//System.out.println(point);

		Rectangle2D.Double search_area = new Rectangle2D.Double(point.x - Model.ROAD_SEARCH_DISTANCE,
				point.y - Model.ROAD_SEARCH_DISTANCE,
				2*Model.ROAD_SEARCH_DISTANCE,
				2*Model.ROAD_SEARCH_DISTANCE);
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
		if(closest != null && distance < 200){
			//System.out.printf("found road: "+closest.roadname+" %.2f meters away\n",distance);
			return closest.roadname;
		}
		return " ";
	}
}
