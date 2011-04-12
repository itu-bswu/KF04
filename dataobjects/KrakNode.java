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
	private static final long serialVersionUID = -725474151826866827L;
	private float X; // Geographic X, meter East in UTM zone 32
	private float Y; // Geographic Y, meter North of Equator

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
	public float getX() {
		return X;
	}

	/**
	 * @return The geographic Y coordinate, meter North of Equator
	 */
	public float getY() {
		return Y;
	}

}
