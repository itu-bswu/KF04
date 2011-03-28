import java.awt.geom.Rectangle2D;
import java.util.Set;

public class QuadTree<T extends KrakEdge>{

	/**
	 * Quad tree node
	 *
	 * Filip giver vejene niveauer.
	 *
	 * Jeg skal gemme vejene i den specifikke quadnote.
	 * Hver quadnote skal have en int v�rdi om hvor dybt den er i systemet.
	 *
	 * Nr quadnotene dannes skal de store edges gemmes i de hje noder
	 *
	 * Nr der bedes om edges skal man angive et dybte niveau man vil g ned
	 *
	 * alle edges med denne eller mindre dybte skal returneres
	 *
	 *
	 * QuadTree note skal have en "isLevel(egde,niveau)" metode til at angive om en vej er en bestemt type.
	 */

	QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		Stopwatch timer = new Stopwatch("Creating QuadTree");
		root = new QuadTreeNode<T>(bounds,content);
		timer.printTime();
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