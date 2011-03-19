import graphlib.Graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

/**
 * Control class for the Map system.
 * 
 * @author Jakob Melnyk
 * @version 11 March - 2011
 */
public class Control {

	private static final double MOVE_LENGTH = 0.30;
	private static final double ZOOM_LENGTH = 0.15;
	private static final String NAME = "Map"; //Name of the window containing the map.
	private final File dataDir = new File(".", "data"); //Where control needs to look for the nodeFile and edgeFile
	private final String nodeFile = "kdv_node_unload.txt"; //The nodes used to construct the graph
	private final String edgeFile = "kdv_unload.txt"; //The edges used to construct the graph
	private View v;
	private Map m;
	private Graph<KrakEdge, KrakNode> g;

	/**
	 * Contstructor for class Control
	 */
	public Control() {
		System.out.println("creating Control");
		try {
			g = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input.");
		}
		System.out.println("Done loading data");
		m = new Map(g);
		v = new View(NAME, m.getLines(), m.getRatio());
		addListeners();
	}

	private void addListeners(){
		//Listener for "move-up" button.
		v.addUpListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.zoom(new Rectangle2D.Double(old.x, old.y + (-1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-down" button.
		v.addDownListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.zoom(new Rectangle2D.Double(old.x, old.y + (1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-left" button.
		v.addLeftListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.zoom(new Rectangle2D.Double(old.x + (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-right" button.
		v.addRightListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.zoom(new Rectangle2D.Double(old.x + (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-in" button.
		v.addInListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.zoom(zoomRect(ZOOM_LENGTH, true, old));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-out" button.
		v.addOutListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.zoom(zoomRect(ZOOM_LENGTH, false, old));
				v.repaint(m.getLines());
			}});
		//Listener for "mouse zoom"
		v.addCanvasMouseListener(new MouseAdapter(){
			private Point a = null;
			private Point b = null;
			private Rectangle2D.Double p = null;
			public void mousePressed(MouseEvent e){
				a = e.getPoint();
			}
			public void mouseReleased(MouseEvent e){
				if(a == null) return;
				b = e.getPoint();
				p = convertPointsToRectangle(a, b);
				double ratio = v.getCanvasWidth() / v.getCanvasHeight();
				if(p.width < p.height){
					p.width = (int) ratio * p.height; 
				}
				else{
					p.height = (int) p.width / ratio;
				}
				m.zoom(
						new Rectangle2D.Double(p.x/v.getCanvasWidth() * m.getBounds().width + m.getBounds().x,
								p.y/v.getCanvasHeight() * m.getBounds().height + m.getBounds().y,
								p.width/v.getCanvasWidth() * m.getBounds().width,
								p.height/v.getCanvasHeight() * m.getBounds().height));
				v.repaint(m.getLines());
			}
		});
	}
	private Rectangle2D.Double zoomRect(double factor, boolean zoom, Rectangle2D.Double old){
		if(zoom){
			return new Rectangle2D.Double(old.x + factor * old.width, //x is increased by the factor in proportion to the width
					old.y + factor * old.height, //y is increased by the factor in proportion to the height
					old.width - factor * old.width, //width is decreased by the factor
					old.height - factor * old.height); //height is decreased by the factor
		}
		else{
			return new Rectangle2D.Double(old.x - old.width * factor, //x is decreased by the factor in proportion to the width
					old.y - old.height * factor, //y is decreased by the factor in proportion to the height
					old.width + old.width * factor, //width is increased by the factor
					old.height + old.height * factor);//height is increased by the factor
		}
	}
	private Rectangle2D.Double convertPointsToRectangle(Point a, Point b){

		Rectangle2D.Double p;
		if(b.x < a.x){
			if(b.y < a.y){
				p = new Rectangle2D.Double(b.x, b.y, (a.x - b.x), (a.y - b.y));
			}
			else{

				p = new Rectangle2D.Double(b.x, a.y, (a.x - b.x), (b.y - a.y));
			}
		}
		else{
			if(b. y < a.y){
				p = new Rectangle2D.Double(a.x, b.y, (b.x - a.x), (a.y - b.y));
			}
			else{
				p = new Rectangle2D.Double(a.x, a.y, (b.x - a.x), (b.y - a.y));
			}
		}
		return p;
	}
}