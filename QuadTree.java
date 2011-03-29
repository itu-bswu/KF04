import java.awt.geom.Rectangle2D;
import java.util.HashSet;
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

	public static final int[] part1 = new int[]{0,1,2,3,4,10,21,22,31,32,33,34,35,41,42,43,44,45,46,80};
	public static final int[] part2 = new int[]{5,11,23,24,25};
	public static final int[] part3 = new int[]{6,8,26,28,48,95,99};

	private QuadTreeNode<T> root1;
	private QuadTreeNode<T> root2;
	private QuadTreeNode<T> root3;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		Stopwatch timer = new Stopwatch("Creating QuadTree");
		devide(bounds,content);
		//root1 = new QuadTreeNode<T>(bounds,content);
		timer.printTime();
	}

	private void devide(Rectangle2D.Double bounds, Set<T> content){
		Set<T> set1 = new HashSet<T>();
		Set<T> set2 = new HashSet<T>();
		Set<T> set3 = new HashSet<T>();

		boolean found;
		for(KrakEdge edge : content){
			found = false;
			// Highways and larger roads
			for(int i : part1){
				if(edge.type == i){
					set1.add((T) edge);
					found = true;
					break;
				}
			}

			// regular sized roads
			if(!found){
				for(int i : part2){
					if(edge.type == i){
						set2.add((T) edge);
						found = true;
						break;
					}
				}
			}

			// smaller roads and paths
			if(!found){
				for(int i : part3){
					if(edge.type == i){
						set3.add((T) edge);
						found = true;
						break;
					}
				}
			}
		}
		root1 = new QuadTreeNode<T>(bounds,set1);
		root2 = new QuadTreeNode<T>(bounds,set2);
		root3 = new QuadTreeNode<T>(bounds,set3);
	}

	/**
	 * Querries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	public Set<T> query(Rectangle2D.Double qarea){
		double area = (qarea.width/1000)*(qarea.height/1000);
		System.out.println("area: "+area+" km2");
		Set<T> total;

		total = root1.query(qarea);
		if(area < 50000){
			System.out.println("searcing layer 2");
			total.addAll(root2.query(qarea));
			if(area < 5000){
				System.out.println("searching layer 3");
				total.addAll(root3.query(qarea));
			}
		}
		return total;
	}
}