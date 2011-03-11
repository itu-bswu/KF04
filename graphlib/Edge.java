package graphlib;

/**
 * Represents an Edge in a graph. Can be both directed an undirected
 * @author Peter Tiedemann petert@itu.dk
 *
 */
public class Edge<N extends Node> {
  public N v1;
  public N v2;
	
  // direction of edge, see FORWARD,BACKWARD and BOTH below
  protected byte direction;
	
  // indicates the edge goes from v1 -> v2
  public static final byte FORWARD =1;
	
  // indicates the edge goes from v2 -> v1
  public static final byte BACKWARD =2;
	
  // indicates both of the above(undirected edge)
  public static final byte BOTH = FORWARD | BACKWARD;
	
  protected Edge() {
  }

  public Edge(N n1, N n2, byte direction){
    v1 = n1;
    v2 = n2;
    this.direction = direction;
  }
	
  /**
   * Returns the start node for a directed edge,
   * otherwise returns an arbitrary node in the edge
   */
  public N getStart(){
    if ((direction & FORWARD) != 0){
      return v1;
    }
    else return v2;
  }
  
  /**
   * Returns the node in the edge that is NOT the node n
   * @param n
   * @return
   */
  public N getOtherEnd(N n){
    if (v1 == n) 
      return v2;
    else 
      return v1;
  }
  
  /**
   * Returns the end node for a directed edge,
   * otherwise returns an arbitrary node in the edge
   */
  public N getEnd(){
    if ((direction & FORWARD) != 0) 
      return v2;
    else 
      return v1;
  }
}
