import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A QuadTree to store KrakEdges for easy access of content at a given area.
 * @author Emil
 *
 * @param <T> The KrakEdge sub-type to store.
 */
public class QuadTreeNode<T extends KrakEdge> {

	public static final int MAX_CONTENT = 10000;

	private Rectangle2D.Double bounds;
	private Set<T> contents;
	private List<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>(4);

	/**
	 * The constructor for creating a QuadTreeNode with the given boundaries and content.
	 * @param bounds The boundaries of the new QuadTreeNode.
	 * @param content The content for the new QuadTreeNode.
	 */
	@SuppressWarnings("unchecked")
	public QuadTreeNode(Rectangle2D.Double bounds, Set<T> content){
		//System.out.print("creating QuadTreeNode of size "+content.size());

		// if there are too much content
		if(content.size() > MAX_CONTENT){
			// The sub-bounds
			Rectangle2D.Double nw = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2.0, bounds.height/2.0);
			Rectangle2D.Double ne = new Rectangle2D.Double(bounds.x+bounds.width/2.0, bounds.y, bounds.width/2.0, bounds.height/2.0);
			Rectangle2D.Double sw = new Rectangle2D.Double(bounds.x, bounds.y+bounds.height/2.0, bounds.width/2.0, bounds.height/2.0);
			Rectangle2D.Double se = new Rectangle2D.Double(bounds.x+bounds.width/2.0, bounds.y+bounds.height/2.0, bounds.width/2.0, bounds.height/2.0);
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

	/**
	 * Tells is the node has any sub-nodes.
	 * @return True if the node has sub-nodes.
	 */
	public boolean isEmpty(){
		return nodes.isEmpty();
	}

	/**
	 * To find the boundaries of the area that the Node covers.
	 * @return The boundaries of the area that the Node covers.
	 */
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
					Set<T> temp = item.query(qarea);
					if(temp.size() > 0){
						if(results.size() == 0){
							results = item.query(qarea);
						}else{
							results.addAll(item.query(qarea));
						}
					}
				}
			}
			//System.out.println("sending back "+results.size()+" edges");
			return results;
		}else{
			return contents;
		}
	}
}