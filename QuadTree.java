import java.awt.geom.Rectangle2D;
import java.util.Set;

public class QuadTree<T extends KrakEdge>{
	
	QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		System.out.println("creating QuadTree");
		root = new QuadTreeNode<T>(bounds,content);
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
