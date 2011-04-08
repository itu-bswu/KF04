package dataobjects;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import utils.Stopwatch;

public class QuadTree<T extends KrakEdge>{

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