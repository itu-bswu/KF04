package dataobjects;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A QuadTree to store KrakEdges for easy access of content at a given area.
 * 
 * @author Emil Juul Jacobsen; Niklas Hansen
 * @param <T> The KrakEdge sub-type to store.
 */
public class QuadTreeNode<T extends KrakEdge> implements Serializable {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 4409394409382684458L;

	public static final int MAX_CONTENT = 500;

	private Rectangle2D.Double bounds;
	private Set<T> contents;
	private QuadTreeNode<T> nw, ne, sw, se;
	QuadTreeNode[] nodes = { nw, ne, sw, se };

	/**
	 * The constructor for creating a QuadTreeNode with the given boundaries and content.
	 * @param bounds2 The boundaries of the new QuadTreeNode.
	 * @param content The content for the new QuadTreeNode.
	 */
	public QuadTreeNode(Rectangle2D.Double bounds2, Set<T> content){

		// if there are too much content
		if(content.size() > MAX_CONTENT){
			// The sub-bounds
			Rectangle2D.Double nw = new Rectangle2D.Double(bounds2.x, bounds2.y, bounds2.width/2.0f, bounds2.height/2.0);
			Rectangle2D.Double ne = new Rectangle2D.Double(bounds2.x+bounds2.width/2.0, bounds2.y, bounds2.width/2.0, bounds2.height/2.0);
			Rectangle2D.Double sw = new Rectangle2D.Double(bounds2.x, bounds2.y+bounds2.height/2.0, bounds2.width/2.0f, bounds2.height/2.0);
			Rectangle2D.Double se = new Rectangle2D.Double(bounds2.x+bounds2.width/2.0, bounds2.y+bounds2.height/2.0, bounds2.width/2.0, bounds2.height/2.0);
			Double[] rects = new Rectangle2D.Double[]{nw,ne,sw,se};

			// the sets of Krakedges
			Set<T> nw_set = new HashSet<T>();
			Set<T> ne_set = new HashSet<T>();
			Set<T> sw_set = new HashSet<T>();
			Set<T> se_set = new HashSet<T>();
			Set<T>[] sets = new Set[] { nw_set, ne_set, sw_set, se_set };

			// putting the edges into the right boxes
			for(KrakEdge edge : content){
				for(int i = 0 ; i < 4 ; i++){
					if(rects[i].intersectsLine(edge.getLine())){
						sets[i].add((T) edge);
					}
				}
			}

			// saving all 4 nodes
			for(int i = 0 ; i < 4 ; i++){
				nodes[i] = new QuadTreeNode<T>(rects[i], sets[i]);
			}

		}else{
			contents = content;
		}
		this.bounds = bounds2;
	}

	/**
	 * Tells is the node has any sub-nodes.
	 * @return True if the node has sub-nodes.
	 */
	public boolean isEmpty(){
		boolean returnVal = true;
		for (int i = 0; i < nodes.length; i++) {
			returnVal = returnVal && nodes[i] == null;
		}
		return returnVal;
	}

	/**
	 * To find the boundaries of the area that the Node covers.
	 * @return The boundaries of the area that the Node covers.
	 */
	public Rectangle2D.Double getBounds(){
		return bounds;
	}

	/**
	 * Queries the node for KrakEdges with a specific rectangle
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
}
