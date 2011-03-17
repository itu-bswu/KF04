import java.awt.geom.Point2D;

import graphlib.Node;

/**
 * A graph node, created from a NodeData object. Note that KDV_ID is used as a
 * global id number, while KDV# is assumed to be continuous in the loaded file.
 */
public class KrakNode extends Node {
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

}
