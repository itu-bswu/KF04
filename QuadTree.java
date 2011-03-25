import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
	 * N�r quadnotene dannes skal de store edges gemmes i de h�je noder
	 * 
	 * N�r der bedes om edges skal man angive et dybte niveau man vil g� ned
	 * 
	 * alle edges med denne eller mindre dybte skal returneres
	 * 
	 * 
	 * QuadTree note skal have en "isLevel(egde,niveau)" metode til at angive om en vej er en bestemt type.
	 */
	
	QuadTreeNode<T> root;
	private Set<T> content;

	public QuadTree(Rectangle2D.Double bounds, Set<T> content){
		this.content = content;
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
	
	private Set<KrakEdge> getEdges(int zoomLevel){
		ArrayList<Integer> allowedRoadTypes = new ArrayList<Integer>();
		Set<KrakEdge> res = new HashSet<KrakEdge>();
		switch(zoomLevel){
		case 1:
			//moterveje, motortrafikveje
			allowedRoadTypes.add(1);
			allowedRoadTypes.add(2);
			allowedRoadTypes.add(31);
			allowedRoadTypes.add(32);
			allowedRoadTypes.add(41);
			allowedRoadTypes.add(42);
			break;
		case 2:
			//prim�rrute > 6
			allowedRoadTypes.add(3);
			allowedRoadTypes.add(33);

			break;
		case 3:
			//sekund�rrute > 6 meter
			allowedRoadTypes.add(4);
			allowedRoadTypes.add(33);

			break;
		case 4:
			//vej 3 - 6 meter
			allowedRoadTypes.add(5);

		case 5:
			//anden vej
			allowedRoadTypes.add(6);
		case 6:
			//sti
			allowedRoadTypes.add(8);
		case 7:
			//resten
			allowedRoadTypes.add(10);
			allowedRoadTypes.add(11);
			allowedRoadTypes.add(80);
			allowedRoadTypes.add(95);
			allowedRoadTypes.add(99);
		}
		for(KrakEdge krakEdge: content){
			for(int allowedRoadType : allowedRoadTypes){
				if(allowedRoadType==krakEdge.type){
					res.add(krakEdge);
					break;
					}
				}
			}
		return res;
	}
}
