import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuadTreeNode<T extends KrakEdge> {

	public static final int MAX_CONTENT = 1337;

	private Rectangle2D.Double bounds;
	private Set<T> contents = new HashSet<T>();
	private List<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>(4);

	public QuadTreeNode(Rectangle2D.Double bounds, Set<T> content){
		if(content.size() > MAX_CONTENT){
			// The sub-bounds
			Rectangle2D.Double nw = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2, bounds.height/2);
			Rectangle2D.Double ne = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2, bounds.height/2);
			Rectangle2D.Double sw = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2, bounds.height/2);
			Rectangle2D.Double se = new Rectangle2D.Double(bounds.x, bounds.y, bounds.width/2, bounds.height/2);
			
			
			
		}else{
			contents = content;
			this.bounds = bounds;
		}
	}

	public boolean isEmpty(){
		return nodes.isEmpty();
	}
	
	public Rectangle2D.Double getBounds(){
		return bounds;
	}

	public Set<T> query(Rectangle qarea){
		Set<T> results = new HashSet<T>();

		for(QuadTreeNode<T> item : nodes){
			// if sub-node is within rectangle
			if(item.getBounds().intersects(qarea)){
				results.addAll(item.query(qarea));
			}
		}
		return results;
	}

}
