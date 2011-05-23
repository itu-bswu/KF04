package dataobjects;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Set;

import utils.Stopwatch;

/**
 * The quadtree datastructure, for easy access of content at a given area. 
 * Works by constantly splitting the data in four, until the data amount 
 * is small enough.
 * 
 * @author Emil Juul Jacobsen; Niklas Hansen
 * @param <T> KrakEdge (or sub-type)
 */
public class QuadTree<T extends KrakEdge> implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -8503471615911218684L;
	
	private QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		Stopwatch timer = new Stopwatch("Creating QuadTree");
		root = new QuadTreeNode<T>(bounds,content);
		timer.printTime();
	}

	/**
	 * Queries the QuadTree for data within a given Rectangle
	 * @param qarea The Rectangle to search within.
	 * @return
	 */
	public Set<T> query(Rectangle2D.Double qarea){
		return root.query(qarea);
	}
}