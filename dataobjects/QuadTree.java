package dataobjects;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.Serializable;
import java.util.Set;

import utils.Stopwatch;

public class QuadTree<T extends KrakEdge> implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -8503471615911218684L;
	
	/**
	 * Quad tree node
	 *
	 */
	private QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		Stopwatch timer = new Stopwatch("Creating QuadTree");
		root = new QuadTreeNode<T>(bounds,content);
		timer.printTime();
	}


	public Set<T> query(Rectangle2D.Double qarea){
		return root.query(qarea);
	}
}