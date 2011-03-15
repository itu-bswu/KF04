import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

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
	private View v;
	private Map m;
	
	/**
	 * Contstructor for class Control
	 */
	public Control() {
		m = new Map();
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
							m.zoom(new Point2D.Double(ZOOM_LENGTH, ZOOM_LENGTH), new Point2D.Double(1-ZOOM_LENGTH, 1-ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
		v.addOutListener(new ActionListener(){
						public void actionPerformed(ActionEvent arg0){
							m.zoom(new Point2D.Double(-ZOOM_LENGTH, -ZOOM_LENGTH), new Point2D.Double(1+ZOOM_LENGTH, 1+ZOOM_LENGTH));
							v.repaint(m.getLines());
						}});
	}
}
