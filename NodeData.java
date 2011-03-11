import java.io.IOException;

/**
 * An object storing the raw node data from the krak data file.
 */
public class NodeData {
	final int ARC;
	int KDV; // NB: Written by InstanceCreator
	final int KDV_ID;
	final double X_COORD;
	final double Y_COORD;
	
	/**
	 * Parses node data from line, throws an IOException
	 * if something unexpected is read
	 * @param line The source line from which the NodeData fields are parsed
	 * @throws IOException
	 */
	public NodeData(String line) throws IOException{
		DataLine dl = new DataLine(line);
		ARC = dl.getPositiveInt();
		KDV = dl.getPositiveInt();
		KDV_ID = dl.getPositiveInt();
		X_COORD = dl.getPositiveDouble();
		Y_COORD = dl.getPositiveDouble();
	}
	
	/**
	 * Returns a string representing the node data in the same format as used in the
	 * kdv_node_unload.txt file.
	 */
	public String toString(){
		return "" + ARC + "," + KDV + "," + KDV_ID + "," + X_COORD + "," + Y_COORD;
	}
}
