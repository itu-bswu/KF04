import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
}
