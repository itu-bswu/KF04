import graphlib.Graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
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
	private String nodeFile = "C:/Users/Jakob Melnyk/workspace/KF04/src/data/Charlottenlund/kdv_node_unload.txt";
	private String edgeFile = "C:/Users/Jakob Melnyk/workspace/KF04/src/data/Charlottenlund/kdv_unload.txt";
	private View v;
	private Map m;
	private Graph g;
	
	/**
	 * Contstructor for class Control
	 */
	public Control() {
		try {
			g = KrakLoader.graphFromFiles(nodeFile, edgeFile);
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input.");
		}
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
							Rectangle2D.Double i = m.getBounds();
							//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
							m.zoom(new Rectangle2D.Double(i.x + i.width * ZOOM_LENGTH, i.y + i.height * ZOOM_LENGTH, i.width - i.width * ZOOM_LENGTH, i.height - i.height * ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
		//Listener for "zoom-out" button.
		v.addOutListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							Rectangle2D.Double i = m.getBounds();
							//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
							m.zoom(new Rectangle2D.Double(i.x - i.width * ZOOM_LENGTH, i.y - i.height * ZOOM_LENGTH, i.width + i.width * ZOOM_LENGTH, i.height + i.height * ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
		}
}