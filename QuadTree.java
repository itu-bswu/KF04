import java.awt.geom.Rectangle2D;
import java.util.Set;

public class QuadTree<T extends KrakEdge>{
	
	/**
	 * Quad tree node
	 * 
	 * Filip giver vejene niveauer.
	 * 
	 * Jeg skal gemme vejene i den specifikke quadnote.
	 * Hver quadnote skal have en int værdi om hvor dybt den er i systemet.
	 * 
	 * Når quadnotene dannes skal de store edges gemmes i de høje noder
	 * 
	 * Når der bedes om edges skal man angive et dybte niveau man vil gå ned
	 * 
	 * alle edges med denne eller mindre dybte skal returneres
	 * 
	 * 
	 * QuadTree note skal have en "isLevel(egde,niveau)" metode til at angive om en vej er en bestemt type.
	 */
	
	QuadTreeNode<T> root;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content) {
		System.out.print("creating QuadTree ... ");
		Stopwatch timer = new Stopwatch();
		int zoomLevel = 0;
		
		// Kald divide funktionen. Lav derefter en ny node der får sættet med de største veje. (set1)
		
		root = new QuadTreeNode<T>(bounds,content,zoomLevel);
		System.out.printf("%.2f sec\n",timer.getTime());
	}

	/**
	 * Querries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	public Set<T> query(Rectangle2D.Double qarea, int zoomLevel){
		return root.query(qarea, zoomLevel);
	}
	
	/**
	 * Divide the types of roads into different sets.
	 * 
	 * If zoomLevel is too high, return null;
	 */
	public static Set<KrakEdge> getEdges(int zoomLevel) {
		return null;
	}
	
	
}
