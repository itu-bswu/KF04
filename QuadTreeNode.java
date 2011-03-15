import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class QuadTreeNode<T> {
	Rectangle bounds;
	List<T> contents = new ArrayList<T>();
	List<QuadTreeNode<T>> nodes = new ArrayList<QuadTreeNode<T>>(4);
	
	public boolean isEmpty(){
		return nodes.isEmpty();
	}
	
	public List<T> query(Rectangle qarea){
		//TODO implement query
		List<T> results = new ArrayList<T>();
		for(T item : contents){
			//qarea.intersects(item.bounds);
		}
		return contents;
	}
	
}
