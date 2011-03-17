import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuadTreeNode<T extends KrakEdge> {

	public static final int MAX_CONTENT = 10000;

	private Rectangle2D.Double bounds;
	private Set<T> contents;
	private List<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>(4);

	@SuppressWarnings("unchecked")
	public QuadTreeNode(Rectangle2D.Double bounds, Set<T> content){
		//System.out.print("creating QuadTreeNode of size "+content.size());
		
		// if there are too much content
		if(content.size() > MAX_CONTENT){
			// The sub-bounds
			Rectangle2D.Double nw = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2, bounds.height/2);
			Rectangle2D.Double ne = new Rectangle2D.Double(bounds.x+bounds.width/2, bounds.y, bounds.width/2, bounds.height/2);
			Rectangle2D.Double sw = new Rectangle2D.Double(bounds.x, bounds.y+bounds.height/2, bounds.width/2, bounds.height/2);
			Rectangle2D.Double se = new Rectangle2D.Double(bounds.x+bounds.width/2, bounds.y+bounds.height/2, bounds.width/2, bounds.height/2);
			Rectangle2D.Double[] rects = new Rectangle2D.Double[]{nw,ne,sw,se};

			// the sets of Krakedges
			Set<T> nw_set = new HashSet<T>();
			Set<T> ne_set = new HashSet<T>();
			Set<T> sw_set = new HashSet<T>();
			Set<T> se_set = new HashSet<T>();
			ArrayList<Set<T>> sets = new ArrayList<Set<T>>();
			sets.add(nw_set);
			sets.add(ne_set);
			sets.add(sw_set);
			sets.add(se_set);

			// putting the edges into the right boxes
			for(KrakEdge edge : content){
				for(int i = 0 ; i < 4 ; i++){
					if(rects[i].intersectsLine(edge.getLine())){
						sets.get(i).add((T) edge);
					}
				}
			}
			
			// saving all 4 nodes
			for(int i = 0 ; i < 4 ; i++){
				nodes.add(new QuadTreeNode<T>(rects[i], sets.get(i)));
			}

		}else{
			contents = content;
		}
		this.bounds = bounds;
		
	}

<<<<<<< HEAD
public class QuadTreeNode<T extends QuadTreeNode> {
	public Rectangle bounds;
	private List<T> contents = new ArrayList<T>();
	private List<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>(4);
	
	public boolean isEmpty(){
		return nodes.isEmpty();
	}
	
	public List<T> query(Rectangle qarea){
		//TODO implement query
		List<T> results = new ArrayList<T>();
		for(T item : contents){
			qarea.intersects(item.bounds);
		}
		
		for(QuadTreeNode<T> node : nodes){
			if(node.isEmpty()){
				continue;
			}
			if(node.bounds.contains(qarea){
				results.add(node.query(qarea));
				break;
			}
		}
		
		return results;
	}
=======
	public boolean isEmpty(){
		return nodes.isEmpty();
	}

	public Rectangle2D.Double getBounds(){
		return bounds;
	}

	/**
	 * Querries the node for KrakEdges with a specific rectangle
	 * @param qarea The rectangle for which to find all KrakEdges
	 * @return A Set with all KrakEdges within the given Rectangle
	 */
	public Set<T> query(Rectangle2D.Double qarea){
		if(!isEmpty()){
			Set<T> results = new HashSet<T>();
			for(QuadTreeNode<T> item : nodes){
				// if sub-node is within rectangle
				if(item.getBounds().intersects(qarea)){
					results.addAll(item.query(qarea));
				}
			}
			return results;
		}else{
			return contents;
		}
	}

>>>>>>> 46d1447fd95a36399c759b3135d34133b388d8ba
}
