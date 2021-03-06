package dataobjects;
import java.awt.geom.Point2D;
import java.io.Serializable;

import loader.NodeData;
import graphlib.Node;

/**
 * A graph node, created from a NodeData object. Note that KDV_ID is used as a
 * global id number, while KDV# is assumed to be continuous in the loaded file.
 */
public class KrakNode extends Node implements Serializable {
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1678781033332952834L;
	
	private double X; // Geographic X, meter East in UTM zone 32
	private double Y; // Geographic Y, meter North of Equator

	/**
	 * Constructor for objects of class KrakNode.
	 * @param data NodeData object.
	 */
	public KrakNode(NodeData data) {
		super(data.KDV);
		X = data.X_COORD;
		Y = data.Y_COORD;
	}
	
	/**
	 * Get the points of the node.
	 * 
	 * @return Point object.
	 */
	public Point2D.Double getPoint(){
		return new Point2D.Double(X,Y);
	}
	
	/**
	 * @return The geographic X coordinate, meter East in UTM zone 32
	 */
	public double getX() {
		return X;
	}

	/**
	 * @return The geographic Y coordinate, meter North of Equator
	 */
	public double getY() {
		return Y;
	}

	/**
	 * The (crow flying) distance to another node
	 * @param that The other node
	 * @return The distance
	 */
	public double distanceTo(KrakNode that){
		double x_part = Math.pow(this.getX()-that.getX(),2);
		double y_part = Math.pow(this.getY()-that.getY(),2);
		//System.out.println("x="+x_part+", y="+y_part);
		return Math.sqrt(x_part + y_part);
	}
}
