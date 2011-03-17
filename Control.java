import graphlib.Graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**
 * Control class for Map system.
 * 
 * @author Jakob Melnyk
 * @version 11 March - 2011
 */
public class Control {
	
	private static final double MOVE_LENGTH = 0.30;
	private static final double ZOOM_LENGTH = 0.15;
	private static final String NAME = "Map"; //Name of the window containing the map.
	//private final File dataDir = new File(".", "data"); //Where control needs to look for the nodeFile and edgeFile
	private final String nodeFile = "C:/Users/Jakob Melnyk/workspace/KF04/src/data/kdv_node_unload.txt"; //The nodes used to construct the graph
	private final String edgeFile = "C:/Users/Jakob Melnyk/workspace/KF04/src/data/kdv_unload.txt"; //The edges used to construct the graph
	private View v;
	private Map m;
	private Graph<KrakEdge, KrakNode> g;
	/**
	 * Contstructor for class Control
	 */
	public Control() {
		System.out.println("creating Control");
		try {
			g = KrakLoader.graphFromFiles(nodeFile, edgeFile);
			//g = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input.");
		}
		System.out.println("Done loading data");
		m = new Map(g);
		v = new View(NAME, m.getLines());
		addListeners();
	}
	
	private void addListeners(){
		//Listener for "move-up" button.
		v.addUpListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.NORTH, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		//Listener for "move-down" button.
		v.addDownListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.SOUTH, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		//Listener for "move-left" button.
		v.addLeftListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.WEST, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		//Listener for "move-right" button.
		v.addRightListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.EAST, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		//Listener for "zoom-in" button.
		v.addInListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
							Rectangle2D.Double old = m.getBounds();
							m.zoom(new Rectangle2D.Double(old.x+ZOOM_LENGTH*old.width, //x is increased by the zoom_length in proportion to the width
									old.y+ZOOM_LENGTH*old.height, //y is increased by the zoom_length in proportion to the height
									old.width-ZOOM_LENGTH * old.width, //width is decreased by the zoom_length
									old.height-ZOOM_LENGTH * old.height)); //height is decreased by the zoom_length
							v.repaint(m.getLines());
						}});
		//Listener for "zoom-out" button.
		v.addOutListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
							Rectangle2D.Double old = m.getBounds();
							m.zoom(new Rectangle2D.Double(old.x - old.width * ZOOM_LENGTH, //x is decreased by the zoom_length in proportion to the width
									old.y - old.height * ZOOM_LENGTH, //y is decreased by the zoom_length in proportion to the height
									old.width + old.width * ZOOM_LENGTH, //width is increased by the zoom_length
									old.height + old.height * ZOOM_LENGTH)); //height is increased by the zoom_length
							v.repaint(m.getLines());
						}});
		}
}
