import java.awt.geom.Rectangle2D;
import java.util.Set;

public class QuadTree<T extends KrakEdge>{

	QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		System.out.print("creating QuadTree ... ");
		Stopwatch timer = new Stopwatch();
		root = new QuadTreeNode<T>(bounds,content);
		System.out.printf("%.2f sec\n",timer.getTime());
	}

	/**
	 * Querries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	public Set<T> query(Rectangle2D.Double qarea){
		return root.query(qarea);
	}
}
