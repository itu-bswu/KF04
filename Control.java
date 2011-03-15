import graphlib.Graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
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
	private static final String NAME = "Map";
	private final File dataDir = new File(".", "data");
	private final String nodeFile = "kdv_node_unload.txt";
	private final String edgeFile = "kdv_unload.txt";
	private View v;
	private Map m;
	private Graph g;
	
	/**
	 * Contstructor for class Control
	 */
	public Control() {
		try {
			//g = KrakLoader.graphFromFiles(nodeFile, edgeFile);
			g = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			//TODO Catch exception
			e.printStackTrace();
		}
		m = new Map(g);
		v = new View(NAME, m.getLines());
		addListeners();
	}
	
	private void addListeners(){
		v.addUpListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.NORTH, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		v.addDownListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.SOUTH, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		v.addLeftListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.WEST, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		v.addRightListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.move(Direction.EAST, MOVE_LENGTH);
							v.repaint(m.getLines());
						}});
		v.addInListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							//m.zoom(new Point2D.Double(ZOOM_LENGTH, ZOOM_LENGTH), new Point2D.Double(1-ZOOM_LENGTH, 1-ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
		v.addOutListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							//m.zoom(new Point2D.Double(-ZOOM_LENGTH, -ZOOM_LENGTH), new Point2D.Double(1+ZOOM_LENGTH, 1+ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
	}
}
