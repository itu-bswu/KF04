import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import graphlib.Graph;

/**
 * Map class
 */
public class Map {

	private static final float ROAD_SEARCH_DISTANCE = 200;

	private Rectangle2D.Double bounds;
	private final Rectangle2D.Double maxBounds;
	private QuadTree<KrakEdge> qt;

	/**
	 * Constructor
	 * Initialize variables. 
	 * Set the map to look at the entire graph.
	 */
	public Map(Graph<KrakEdge,KrakNode> graph) {
		maxBounds = outerBounds(graph.getNodes());
		bounds = originalBounds();
		this.qt = new QuadTree<KrakEdge>(bounds,graph.getAllEdges());
	}

	/**
	 * Update the bounds
	 * @param view The rectangle of the view to zoom to.
	 */
	public void updateBounds(Rectangle2D.Double bounds) {
		this.bounds = bounds;
	}

	/**
	 * Get the Width of the bounds
	 * @return The width of the bounds
	 */
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
	 * Get the the bounds of the smallest possible rectangle, still showing the entire graph.
	 * @param list 
	 * @return The outer bounds
	 */
	private Rectangle2D.Double outerBounds(List<KrakNode> list) {
		float minX = -1;
		float minY = -1;
		float maxX = -1;
		float maxY = -1;

		for(KrakNode node : list) {

			if (node == null) continue;

			if ((node.getX() < minX)||(minX == -1)) minX = (float) node.getX();
			if ((node.getX() > maxX)||(maxX == -1)) maxX = (float) node.getX();
			if ((node.getY() < minY)||(minY == -1)) minY = (float) node.getY();
			if ((node.getY() > maxY)||(maxY == -1)) maxY = (float) node.getY();
		}

		return new Rectangle2D.Double(minX,minY,maxX-minX,maxY-minY);
	}

	/**
	 * Get all lines corresponding to the edges shown in the map. 
	 * @return All the lines.
	 */
	public Collection<Line> getLines() {
//		Stopwatch timer = new Stopwatch("Making Lines");
		HashSet<Line> lines = new HashSet<Line>();
		for (KrakEdge e : qt.query(bounds)) {
			Point2D.Double firstPoint = relativePoint(new Point2D.Double(e.getStart().getX(),e.getStart().getY()));
			Point2D.Double secondPoint = relativePoint(new Point2D.Double(e.getEnd().getX(),e.getEnd().getY()));
			//Choosing the right color to each line
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
				break;
			case 3:
				//Primærrute > 6 meter
				roadColor = Color.YELLOW;
				break;
			case 4:
				//Sekundærrute > 6 meter
				roadColor = Color.YELLOW;
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
				break;
			case 32:
				//Motortrafikvejsafkørsel
				roadColor = Color.RED;
				break;
			case 33:
				//Primærvejsafkørsel
				roadColor = Color.YELLOW;
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
				break;
			case 42:
				//Motortrafikvejstunnel
				roadColor = Color.RED;
				break;

			}
			lines.add(new Line(firstPoint,secondPoint,roadColor,thickness));
			// TODO: Thickness (last argument) should be chosen
		}
//		timer.printTime();
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
	 * 
	 * @param point
	 * @return
	 */
	public String getClosestRoad(Point2D.Double point){
		//System.out.println("Finding closest road");
		// get all nearby roads

		//System.out.println(point);

		Rectangle2D.Double search_area = new Rectangle2D.Double(point.x - Map.ROAD_SEARCH_DISTANCE,
				point.y - Map.ROAD_SEARCH_DISTANCE,
				2*Map.ROAD_SEARCH_DISTANCE,
				2*Map.ROAD_SEARCH_DISTANCE);
		Set<KrakEdge> all = qt.query(search_area);

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
