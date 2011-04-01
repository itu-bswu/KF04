package loader;
import java.io.IOException;


/**
 * An object storing the raw node data from the krak data file.
 */
public class NodeData {
	public final int ARC;
	public int KDV; // NB: Written by InstanceCreator
	public final int KDV_ID;
	public final float X_COORD;
	public final float Y_COORD;

	/**
	 * Parses node data from line, throws an IOException if something unexpected
	 * is read
	 * 
	 * @param line The source line from which the NodeData fields are parsed
	 * @throws IOException
	 */
	public NodeData(String line) throws IOException {
		DataLine dl = new DataLine(line);
		ARC = dl.getPositiveInt();
		KDV = dl.getPositiveInt();
		KDV_ID = dl.getPositiveInt();
		X_COORD = dl.getPositiveFloat();
		Y_COORD = dl.getPositiveFloat();
	}

	/**
	 * Returns a string representing the node data in the same format as used in
	 * the kdv_node_unload.txt file.
	 */
	public String toString() {
		return "" + ARC + "," + KDV + "," + KDV_ID + "," + X_COORD + ","
				+ Y_COORD;
	}
}
