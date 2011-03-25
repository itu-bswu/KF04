import graphlib.Graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
	//private Graph<KrakEdge, KrakNode> g;

	/**
	 * Contstructor for class Control
	 */
	public Control() {
		System.out.println("creating Control");
		Graph<KrakEdge, KrakNode> g = null;
		printRAM();
		try {
			g = KrakLoader.graphFromFiles(new File(dataDir, nodeFile).getAbsolutePath(), new File(dataDir, edgeFile).getAbsolutePath());
		} catch (IOException e) {
			System.out.println("A problem occured when trying to read input.");
		}
		printRAM();
		System.out.println("Done loading data");
		m = new Map(g);
		printRAM();
		v = new View(NAME, m.getRatio());
		printRAM();
		v.repaint(m.getLines());
		addListeners();
		printRAM();
	}

	private void addListeners(){
		//Listener for "move-up" button.
		v.addUpListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x, old.y + (1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-down" button.
		v.addDownListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x, old.y - (1 * old.getHeight() * MOVE_LENGTH), old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-left" button.
		v.addLeftListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x - (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "move-right" button.
		v.addRightListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(new Rectangle2D.Double(old.x + (1 * old.width * MOVE_LENGTH), old.y, old.width, old.height));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-in" button.
		v.addInListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(zoomRect(ZOOM_LENGTH, true, old));
				v.repaint(m.getLines());
			}});
		//Listener for "zoom-out" button.
		v.addOutListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				//Constructs a new rectangle using the maps bounds and the ZOOM_LENGTH variable.
				Rectangle2D.Double old = m.getBounds();
				m.updateBounds(zoomRect(ZOOM_LENGTH, false, old));
				v.repaint(m.getLines());
			}});
		//Listener for "mouse zoom"
		v.addCanvasMouseListener(new MouseAdapter(){
			private Point a = null;
			private Point b = null;
			private Rectangle2D.Double p = null;
			private Rectangle2D.Double temp = null;
			
			public void mousePressed(MouseEvent e){
				a = e.getPoint();
				a.y = v.getCanvasHeight() -  a.y;
			}
			
			public void mouseReleased(MouseEvent e){
				if(a == null) return; //Tries to catch null pointer from weird mouse events. 
				b = e.getPoint();
				//b.y = v.getCanvasHeight() - b.y;
				p = pointsToRectangle(a, b);
				temp = pointsToRectangle(a, b);
				
				if(temp.width < v.getCanvasWidth()/100 || temp.height < v.getCanvasHeight()/100) return; //Prevents the user from zooming in way too much.
				
				if(v.getCanvasHeight() * (temp.width/v.getCanvasWidth()) > temp.height){
					p.height = v.getCanvasHeight() * temp.width/v.getCanvasWidth();
					p.y = temp.y - (p.height - temp.height) / 2;
				}
				else{
					p.width = v.getCanvasWidth() * temp.height/v.getCanvasHeight();
					p.x = temp.x - (p.width - temp.width) / 2;
				}
				m.updateBounds(
						point2DToRectangle(
								pixelToUTM(new Point((int) p.x, (int) p.y)),
								pixelToUTM(new Point((int) (p.width + p.x), (int) (p.height + p.y))
								)));
						/*(p.x/v.getCanvasWidth()) * m.getBounds().width + m.getBounds().x,
						((p.y)/v.getCanvasHeight()) * m.getBounds().height + m.getBounds().y,
						(p.width/v.getCanvasWidth()) * m.getBounds().width,
						(p.height/v.getCanvasHeight()) * m.getBounds().height)*/
				v.repaint(m.getLines());
			}
			
			// display closest road's name
			public void mouseClicked(MouseEvent e){
				System.out.println("mouse clicked");
				// set label to closest road
				v.setLabel(m.getClosestRoad(pixelToUTM(e.getPoint())));
				System.out.println("done with road finding");
			}
		});
		//
		v.addCanvasComponentListener(new ComponentAdapter(){
			private int oldWidth = v.getCanvasWidth();
			private int oldHeight = v.getCanvasHeight();
			
			public void componentResize(ComponentEvent e){
				//TODO implement this shit
			}
		});
	}
	
	private Rectangle2D.Double zoomRect(double factor, boolean zoom, Rectangle2D.Double old){
		if(zoom){
			return new Rectangle2D.Double(old.x + factor * old.width, //x is increased by the factor in proportion to the width
					old.y + factor * old.height, //y is increased by the factor in proportion to the height
					old.width - factor * old.width * 2, //width is decreased by the factor
					old.height - factor * old.height * 2); //height is decreased by the factor
		}
		else{
			return new Rectangle2D.Double(old.x - old.width * factor, //x is decreased by the factor in proportion to the width
					old.y - old.height * factor, //y is decreased by the factor in proportion to the height
					old.width + old.width * factor * 2, //width is increased by the factor
					old.height + old.height * factor * 2);//height is increased by the factor
		}
	}
	
	private Rectangle2D.Double pointsToRectangle(Point a, Point b){
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
	
	private Rectangle2D.Double point2DToRectangle(Point2D.Double a, Point2D.Double b){
		return new Rectangle2D.Double(a.x, a.y, (b.x - a.x), (b.y - a.y));
	}
	
	private static void printRAM(){
		System.out.println("Used Memory: "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1024*1024)+" mb");
	}
	
	private Point2D.Double pixelToUTM(Point e){
		Rectangle2D.Double map = m.getBounds();
		e.y = v.getCanvasHeight() - e.y;
		// convert pixel to meters
		double x_m = map.x + (e.getX()/v.getCanvasWidth()) * map.width;
		double y_m = map.y + (e.getY()/v.getCanvasHeight()) * map.height;
		return new Point2D.Double(x_m, y_m);
		}
}
