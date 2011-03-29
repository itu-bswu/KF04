import graphlib.Edge;
import graphlib.Graph;

import java.awt.geom.Line2D;
import java.util.HashMap;

/**
 * A graph edge created from an EdgeData object. For the sake of memory
 * efficiency a KrakEdge doesnt store all the information available in EdgeData,
 * for example the zip code and similar are not currently stored. This can
 * easily be changed, by simply adding the necessary fields,and setting their
 * value corresponding to the EdgeData object
 * 
 * @author Peter Tiedemann petert@itu.dk
 */
public class KrakEdge extends Edge<KrakNode> {
	// sestoft: For sharing roadname strings
	private static HashMap<String, String> interner = new HashMap<String, String>();
	public final float length;
	public final int type;
	public final String roadname;
	public final int DAV_DK, DAV_DK_ID;
	public final int FROMLEFT;
	public final int TOLEFT;
	public final int FROMRIGHT;
	public final int TORIGHT;

	/**
	 * Constructor for objects of class KrakEdge.
	 * 
	 * @param data The EdgeData object for KrakEdge.
	 * @param graph The graph object.
	 */
	public KrakEdge(EdgeData data, Graph<KrakEdge, KrakNode> graph) {
		this.n1 = graph.getNode(data.FNODE);
		this.n2 = graph.getNode(data.TNODE);

		length = (float) data.LENGTH;
		DAV_DK = data.DAV_DK;
		DAV_DK_ID = data.DAV_DK_ID;
		type = data.TYP;
		// sestoft: Share roadname strings to save space
		String interned = interner.get(data.VEJNAVN);
		if (interned != null) {
			roadname = interned;
		} else {
			roadname = data.VEJNAVN;
			interner.put(roadname, roadname);
		}
		String dir = data.ONE_WAY;
		/*
		 * tf = ensrettet modsat digitalise­rings­retning (To-From) ft =
		 * ensrettet i digitaliseringsretning (From-To) n = ingen kørsel tilladt
		 * (henholdsvis typ 8, 21-28) <blank> = ingen ensretning
		 */
		if (dir.equalsIgnoreCase("tf")) {
			this.direction = Edge.BACKWARD;
		} else if (dir.equalsIgnoreCase("ft")) {
			this.direction = Edge.FORWARD;
		} else if (dir.equalsIgnoreCase("n")) {
			// FIXME:this road can not be travelled on, shouldn't really be
			// included
			this.direction = Edge.BOTH;
		} else {
			this.direction = Edge.BOTH;
		}
		FROMLEFT = data.FROMLEFT;
		TOLEFT = data.TOLEFT;
		FROMRIGHT = data.FROMRIGHT;
		TORIGHT = data.TORIGHT;
	}
	
	/**
	 * Convert KrakEdge to Line.
	 * 
	 * @return Line from KrakEdge.
	 */
	public Line2D.Double getLine(){
		return new Line2D.Double(n1.getX(), n1.getY(), n2.getX(), n2.getY());
	}

	/**
	 * Clear KrakEdge.
	 */
	public static void clear() {
		interner = null;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString() {
		return "E("+ (int)(n1.getX()-722467) + "," + (int)(n1.getY()-6183245) + ")";
	}
}
